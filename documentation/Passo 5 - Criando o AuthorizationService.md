
# Passo 5 - Criando o AuthorizationService

Após conseguir recuperar o login do usuário no passo anterior, queremos recuperar o dados do usuário do banco e verificar suas permissões. Para isso iremos criar um `AuthorizationService` que implementa a interface `UserDetailsService` e iremos implementar  o método `loadUserByUsername` que retorna `UserDetails`. A implementação é bastante trivial:

```java
@Service  
@AllArgsConstructor  
public class AuthorizationService implements UserDetailsService {  
  
    private final UserRepository userRepository;  
    @Override  
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {  
        return this.userRepository.findByLogin(username);  
    }}
```

Poderíamos também ter criado uma classe de configuração com um métod que retorna um `@Bean` do tipo `UserDetailsService`.


Passo 7->

--- 

# Referências

