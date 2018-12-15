@echo off 

echo ---Deploying site 

copy d:\home\site\repository\AppDB\target\*.war %DEPLOYMENT_TARGET%\webapps\*.war
D:\home\site\wwwroot\webapps> rename AppDb-1.0.WAR ROOT.WAR

