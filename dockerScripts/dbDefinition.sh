echo "Setting db string to docker compatible one"
DockerString="db"

echo "Docker db string was inserted to application.properties"
sed -i "s/\<localhost\>/$DockerString/g" src/main/resources/application.properties
