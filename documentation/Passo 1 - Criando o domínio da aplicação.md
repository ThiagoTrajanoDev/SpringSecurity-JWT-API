
# Passo 1 - Criando o domínio da aplicação

Primeiramente precisamos criar uma classe `User`, que irá modelar o que é um usuário no banco de dados. Nossa classe precisa implementar a interface `UserDetails`  fornecida pelo Spring Security, essa interface permite armazenar as informações do usuário que depois será encapsulada em objetos do tipo `Authentication`. Para satisfazer o contrato estabelecido com a interface, precisamos implementar todos os seus métodos para tornar nossa classe `User` em uma classe concreta.

Utilizando o JPA, iremos usar as anotações @Entity e @Table para definir que nossa classe mapeia um objeto no nosso banco de dados e para definir o nome da nossa tabela primária (como estamos usando H2 DB, nossa tabela é feita em memória, uma boa escolha para prototipagem). Além de outras anotações que geram boilerplate code em tempo de execução, deixando o código mais limpo.

Iremos definir os campos de id, login, password e role (cargo ou papel do usuário). O id será anotado com `@Id`, para determinar que essa será a _primary key_ da nossa tabela, e com `@GeneratedValue(strategy = GenerationType.IDENTITY)` para deixar explicito que o ID será auto incrementado a partir de uma estratégia definida. O campo login será anotado com `@Column(unique = true)` para indicar que os valores dessa coluna devem ser únicos, evitando que dois usuários registrem uma conta com o mesmo nome de usuário/e-mail. O UserRole é um Enum que determina o cargo do usuário, como sua implementação é trivial, iremos abordá-lo ao final.

Dentre todos os métodos obtidos a partir da implementação de `UserDetails`, o que mais nos interessa no momento é o método `getAuthorities()` que retorna uma Collection de objetos que implementam a interface `GrantedAuthority` em algum nível de sua hierarquia, ou seja, retorna o nível de acesso do usuário dentro da nossa aplicação. Os demais métodos herdados irão vir com o retorno como false, devemos colocar todos para true, visando não gerar qualquer tipo de bloqueio ao nosso User na aplicação. 



```java
@Entity(name = "users")  
@Table(name = "users")  
@NoArgsConstructor  
@AllArgsConstructor  
@EqualsAndHashCode(of = "id")  
@Getter  
@Setter  
public class User implements UserDetails {
    @Id  
    @GeneratedValue(strategy = GenerationType.IDENTITY)  
    private Long id;  
    @Column(unique = true)  
    private String login;  
    private String password;  
    private UserRole role;  
  
    public User(String login, String password, UserRole role) {  
        this.login = login;  
        this.password = password;  
        this.role = role;  
    }  
    @Override  
    public Collection<? extends GrantedAuthority> getAuthorities() {  
        if(this.role == UserRole.ADMIN) {  
            return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"),new SimpleGrantedAuthority("ROLE_USER"));  
        } else return List.of(new SimpleGrantedAuthority("ROLE_USER"));  
    }  
    @Override  
    public String getUsername() {  
        return login;  
    }  
    @Override  
    public boolean isAccountNonExpired() {  
        return true;  
    }  
    @Override  
    public boolean isAccountNonLocked() {  
        return true;  
    }  
    @Override  
    public boolean isCredentialsNonExpired() {  
        return true;  
    }  
    @Override  
    public boolean isEnabled() {  
        return true;  
    }}

```

Implementação do Enum UserRole:
``` java
@Getter  
public enum UserRole {  
    ADMIN("admin"),  
    USER("user");  
    private final String role;  
    UserRole(String role){  
        this.role = role;  
    }  
}
```

OBS: Já existe uma implementação de `User` que implementa `UserDetails` de forma nativa no Spring Security, porém é interessante criar nossa própria implementação para termos o controle da classe.

Passo 2 ->

--- 

# Referências

[UserDetails] (https://docs.spring.io/spring-security/site/docs/current/api/org/springframework/security/core/userdetails/UserDetails.html) 

