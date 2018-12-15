package ie.ulster.exam;

class Equiptment {
    private String Name;
    private int Duration;
    private int Copies;
    private String Allowed;

    public Equiptment() {
    }

    public Equiptment(String name, int Duration, int Copies, String Allowed) {
        this.setName(name);
        this.setDuration(Duration);
        this.setCopies(Copies);
        this.setAllowed(Allowed);
    }

    // setter and getter methods..

    /* @param allowed the Allowed to set */
    public void setAllowed(String allowed) {
        this.Allowed = allowed;
    }

    /* @param copies the Copies to set */
    public void setCopies(int copies) {
        this.Copies = copies;
    }

    /* @param duration the Duration to set */
    public void setDuration(int duration) {
        this.Duration = duration;
    }

    /* @param name the Name to set */
    public void setName(String name) {
        this.Name = name;
    }

    /* @return the name */
    public String getName() {
        return Name;
    }

    /* @return the Duration */
    public int getDuration() {
        return Duration;
    }

    /* @return the Copies */
    public int getCopies() {
        return Copies;
    }

    /* @return the Copies */
    public String IsAllowed() {
        return Allowed;
    }

}