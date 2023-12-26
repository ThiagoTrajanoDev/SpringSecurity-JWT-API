
# Passo 4 - Criando o TokenService ou JwtService

Iremos dar uma pausa no `SecurityFilter` para criar nosso TokenService, que irá gerar e validar os tokens JWT.

Primeiro criamos uma `String secret` que receberá o valor de uma variável de ambiente, esse valor será usado para completar a assinatura (a 3º parte de um JWT) e deverá deverá ser um hash de no mínimo 256 bits, no código foi colocado como "a-very-secure-key"  apenas por preguiça, mas existem geradores de hashs aleatórios na internet que fazem isso muito bem.

Após isso criaremos o método de geração do JWT.  Começaremos criando uma variável `algorithm` do tipo _Algorithm_, que vai ser usado para decidir o tipo do algoritmo de encriptação da assinatura (signing) e depois colocaremos o restante do código dentro um bloco try/catch para capturar as exceções lançadas pelos métodos utilizados. 
O algoritmo utilizado é o HMAC256, sendo o HMAC um algoritmo de geração de hashes e o SHA256 é o algoritmo criptográfico em si.

Dentro do try iremos retornar uma String a partir do encadeamento de métodos, primeiro chamaremos o método static `create` do package `auth0.jwt.JWT`, que retorna um builder de um token. Basta agora adicionar algumas _Claims_: "iss" com o `withIssuer()`, "sub" com `withSubject()`, "exp" como `withExpiresAt()`. O issuer é quem emite o JWT, o subject é um valor único no contexto do emissor ou globalmente único e o tempo de expiração é quando o token irá expirar.
Damos o nome genérico "auth-api" ao issuer, o valor único no nosso contexto será o nome do usuário (login) e o tempo de expiração será de 2 horas. Por fim chamamos o método `sign()` que cria a assinatura do token recebendo nosso _secret_ e retornado o token como uma String.
No catch, capturamos JWTCreationException que pode ser lançado pelo método `sign()` e lançamos um RuntimeException (é indicado a criação de exceções customizadas, mas não iremos fazer aqui, já que não é o foco).

```java
public String generateToken(User user) {  
    try {  
        Algorithm algorithm = Algorithm.HMAC256(secret);  
        return JWT.create()  
                .withIssuer("auth-api")  
                .withSubject(user.getLogin())  
                .withExpiresAt(genExpirationDate())  
                .sign(algorithm);  
    }    catch(JWTCreationException ex) {  
    throw  new RuntimeException("Error while generating token",ex);  
    } 
}
```

Para validar o token, o fluxo é bastante similar. Chamamos o método `require()` passando o algoritmo de validação e é retornado um builder da interface `Verification`. Verificamos quem é o issuer do tolken, fazemos o build recebendo uma instância de `JWTVerifier`, chamamos o método `verify()` passando o token como argumento e retornando um JWT decodificado em caso de conformidade na verificação, ou em caso contrário alguma instância de  `JWTVerificationException`. Após obter o JWT, pegamos o nome de usuário/login com o método `getSubject()`.
No catch retornaremos uma string vazia em caso de erro, já que o objetivo da função é retornar o nome de usuário (a validação de permissões é feita posteriormente com os dados do usuário em mãos).
```java
public String validateToken(String token){  
   try {  
       Algorithm algorithm = Algorithm.HMAC256(secret);  
       return JWT.require(algorithm)  
               .withIssuer("auth-api")  
               .build()  
               .verify(token)  
               .getSubject();  
    }catch(JWTVerificationException ex) {  
       return "";  
  
}
```


Classe completa:
```java
@Service  
public class TokenService {  
  
    @Value("${api.security.token.secret}")  
    private String secret;  
    public String generateToken(User user) {  
        try {  
            Algorithm algorithm = Algorithm.HMAC256(secret);  
            return JWT.create()  
                    .withIssuer("auth-api")  
                    .withSubject(user.getLogin())  
                    .withExpiresAt(genExpirationDate())  
                    .sign(algorithm);  
        }        catch(JWTCreationException ex) {  
        throw  new RuntimeException("Error while generating token",ex);  
        }    
    }     
        
	public String validateToken(String token){  
        try {  
            Algorithm algorithm = Algorithm.HMAC256(secret);  
            return JWT.require(algorithm)  
                    .withIssuer("auth-api")  
                    .build()  
                    .verify(token)  
                    .getSubject();  
		}catch(JWTVerificationException ex) {  
            return "";  
        }     
    }    
    private Instant genExpirationDate() {  
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));  
     }
}
```

Passo 5 ->

--- 

# Referências


[Estrutura JWT] (https://datatracker.ietf.org/doc/html/rfc7519#section-4.1.1)
[Classe JWT] (https://javadoc.io/doc/com.auth0/java-jwt/3.3.0/com/auth0/jwt/JWT.html)