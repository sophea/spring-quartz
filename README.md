# Getting Started
This project is about Springboot with JTW security Integration

# Technologies
    - Java version 1.8
    - Maven 3.x
    - Database Mysql
# Docker
````
    docker run --name mysql -p 3306:3306 -d -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=demo mysql:5.7
````

# Features:

- login jwt api
- create user (normal)
- create admin user



# Demo
````
login : http://localhost:8080/login.html  (admin/abc@2019)


create normal user :

create admin user:

````
 
 
## Generate JWT Private key
 
 There is online tool to generate public/private key (2048 bit): https://www.devglan.com/online-tools/rsa-encryption-decryption
 
 You can write java code to generate this private key
 
 ````
  public static void main(String[] args) throws NoSuchAlgorithmException, JOSEException {
         KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
         kpg.initialize(2048);
         KeyPair kp = kpg.generateKeyPair();
         System.out.println(Base64.getEncoder().encodeToString(kp.getPrivate().getEncoded()));
 
         RSAKey jwk = new RSAKeyGenerator(2048)
                 .keyUse(KeyUse.SIGNATURE) // indicate the intended use of the key
                 .keyID(UUID.randomUUID().toString()) // give the key a unique ID
                 .generate();
 
         // Output the private and public RSA JWK parameters
         System.out.println(jwk);
         System.out.println(Base64.getEncoder().encodeToString(jwk.toPrivateKey().getEncoded()));
     }
  ````