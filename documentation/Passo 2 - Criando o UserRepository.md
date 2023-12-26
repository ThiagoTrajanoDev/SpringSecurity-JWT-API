
# Passo 2 - Criando o UserRepository

Agora iremos criar nosso repository, a camada responsável por fazer operações com os dados dos usuários no banco de dados.

A implementação é extremamente trivial (as maravilhas da tecnologia!). Basta criar a nossa interface e herdar todos os métodos de CRUD da interface JpaRepository, passando o tipo da nossa entidade (User) e o tipo da nossa _primary key_ (Long).

Também podemos implementar alguns métodos personalizados, apenas declarando o método seguindo certo um padrão em sua assinatura, o Spring é inteligente o suficiente para criar uma query no banco. Nesse caso iremos buscar um usuário através do login. O tipo de retorno será algum objeto que sua classe implementa a interface `UserDetails`, poderíamos também (talvez até mais recomendado) retornar um `Optional<User>`, seguindo o padrão de "findByAlgumaCoisa" que  normalmente encontramos por ai, importante frisar que a utilização do valor de retorno seria um pouco diferente, então tenha isso em mente se quiser usar a 2 opção.

Opção 1 (Como iremos utilizar):

```java
public interface UserRepository  extends JpaRepository<User, Long> {  
        UserDetails findByLogin(String login);  
}
```

Opção 2 (Como você poderia fazer):

```java
public interface UserRepository  extends JpaRepository<User, Long> {  
        Optional<User> findByLogin(String login);  
}
```

Passo 3 ->

--- 

# Referências

[JpaRepository] (https://docs.spring.io/spring-data/jpa/docs/current/api/org/springframework/data/jpa/repository/JpaRepository.html)
