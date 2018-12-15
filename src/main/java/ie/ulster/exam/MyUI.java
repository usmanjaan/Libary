package ie.ulster.exam;

import java.util.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.stream.Collectors;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.MultiSelect;
import com.vaadin.ui.Slider;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Grid.SelectionMode;

/**
 * This UI is the application entry point. A UI may either represent a browser
 * window (or tab) or some part of an HTML page where a Vaadin application is
 * embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is
 * intended to be overridden to add component to the user interface and
 * initialize non-component functionality.
 */
@Theme("mytheme")
public class MyUI extends UI {

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        Connection connection = null;
        String connectionString = "jdbc:sqlserver://b00766901-exam.database.windows.net:1433;"
                + "database=B00766901-exam;" + "user=B00766901-exam@b00766901-exam;" + "password=Cloud673;"
                + "encrypt=true;" + "trustServerCertificate=false;" + "hostNameInCertificate=*.database.windows.net;"
                + "loginTimeout=30;";

        final VerticalLayout layout = new VerticalLayout();
        final HorizontalLayout hLayout = new HorizontalLayout();

        Label logo = new Label(
                "<H1>Microsoft Lending Library</H1> <p/> <h3>Please enter the details below and click Book</h3>",
                ContentMode.HTML);

        final TextField name = new TextField();
        name.setCaption("Type your name here:");

        final Slider borrowing = new Slider("How long are you borrowing devices for?", 0, 21);
        borrowing.setWidth("500px");

        final ComboBox lending = new ComboBox<String>();
        lending.setCaption("Offside landing?");
        lending.setItems("No", "Yes");

        Button button = new Button("Book");
        Label message = new Label("Your booking is not complete yet");
        message.setContentMode(ContentMode.HTML);

        Grid<Equiptment> myGrid = new Grid();

        try {
            connection = DriverManager.getConnection(connectionString);
            ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM Equipment");

            List<Equiptment> le = new ArrayList<Equiptment>();

            while (rs.next()) {
                le.add(new Equiptment(rs.getString("Name"), rs.getInt("Max_Lending_Duration"),
                        rs.getInt("Num_of_Copies"), rs.getString("Off_Site_Lending_allowed")));
            }

            // Set the items (List)
            myGrid.setItems(le);
            myGrid.addColumn(Equiptment::getName).setCaption("Devices");
            myGrid.addColumn(Equiptment::getDuration).setCaption("Duration");
            myGrid.addColumn(Equiptment::getCopies).setCaption("Number of Copies");
            myGrid.addColumn(Equiptment::IsAllowed).setCaption("Offsite Allowed");

        } catch (Exception e) {
            // This will show an error message if something went wrong
            layout.addComponent(new Label(e.getMessage()));
        }

        myGrid.setSizeFull();
        myGrid.setSelectionMode(SelectionMode.MULTI);
        MultiSelect<Equiptment> seleted = myGrid.asMultiSelect();
        Set<Equiptment> selet = myGrid.getSelectedItems();

        button.addClickListener(e -> {

            String device = seleted.getValue().stream().map(Equiptment::getName).collect(Collectors.joining(","));
            int lengthValue = seleted.getValue().stream().mapToInt(Equiptment::getDuration).sum();
            int stock = seleted.getValue().stream().mapToInt(Equiptment::getCopies).sum();

            String offsite = seleted.getValue().stream().map(Equiptment::IsAllowed).collect(Collectors.joining(","));

            if (myGrid.getSelectedItems().size() == 0) {
                message.setValue("<strong>Please select at least one piece of equipment!</strong>");
            } else if (name.getValue().isEmpty()) {
                message.setValue("<strong>Please enter lending request name.</strong>");
            } else if (borrowing.getValue() == 0) {
                message.setValue("<strong>Please confirm how long you wish to borrow items for</strong>");
            } else if (stock == 0) {
                message.setValue("<strong>You cannot select an item that is not in stock.</strong>");
            } else if (lending.getValue() != offsite) {
                message.setValue("<strong>This item is not allowed to be taken offsite.</strong>");
            } else {
                message.setValue("Success!  The group is booked now");
            }

        });
        Label studentid = new Label("B00766901");
        layout.addComponent(logo);
        hLayout.addComponents(name, borrowing, lending);
        layout.addComponent(hLayout);
        layout.addComponent(button);
        layout.addComponent(message);
        layout.addComponent(myGrid);
        layout.addComponent(studentid);

        setContent(layout);
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
