echo "Generating random string of 60 chars"
RandomString=$(pwgen 60 1)

echo "Random string was inserted to security constants!"
sed -i "s/\<thisSecretNeedsToBeMovedToASecureLocation\>/$RandomString/g" src/main/java/com/xebia/vulnmanager/auth/security/SecurityConstants.java
