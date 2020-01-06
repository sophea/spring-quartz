# Getting Started
This project is about Springboot with Quartz Integration

# Technologies
    - Java version 1.8
    - Maven 3.x
    - Database Mysql
# Docker
````
    docker run --name mysql -p 3306:3306 -d -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=demo mysql:5.7
````

# Features:

- create new job running with bash script behind
- create new job with java code and cron job expression
- create new job with Simple repeating time
- manage public holiday with CRUD action

   ~~~~
   create : POST /api/public-holiday/v1
   update : POST /api/public-holiday/v1/{id}
   getDetail: GEt /api/public-holiday/v1/{id}
   delete: DELETE /api/public-holiday/v1/{id}
   getPage: GET /api/public-holiday/v1?limit={limit}&offset=${offset}
   ~~~~


# Demo
````
loging : http://localhost:8081/login.html  (admin/abc@2019)

CRUD actions : http://localhost:8081/holiday.html

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