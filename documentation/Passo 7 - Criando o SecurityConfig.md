
# Passo 7 - Criando o SecurityConfig

Agora que temos um filtro para nossas requisições HTTP temos que utilizá-lo de alguma forma.  Para isso criaremos uma classe de configuração anotada com `@Configuration`, `@EnableWebSecurity` para podermos criar métodos que criam beans da interface `SecurityFilterChain`.

Injetaremos o nosso `SecurityFilter` e depois criaremos um `@Bean` de `SecurityFilterChain` que recebe um como argumento um `HttpSecurity`.

Dentro do nosso método desativaremos a proteção contra `csrf` (assunto complexo para cobrir aqui o motivo), após isso faremos o gerenciamento da sessão com `sessionManagement` determinando que o Spring Security não vai criar uma `HttpSession`, quando implementamos o filtro, determinamos que seria do tipo `OncePerRequestFilter`, assim toda requisição deveria ser autenticada, ou seja, nossa aplicação não armazena o estado da autenticação (autenticação Stateless).  Após isso usamos o `authorizeHttpRequests` para determinar quais são os níveis de permissão para as requisições HTTP. Primeiro dizemos que requisições do tipo POST para "/auth/login" serão permitida a todos, requisições do tipo POST para "/auth/register" são permitidas a todos, sessões do tipo POST para "/products" são permitidas apenas para usuários do tipo ADMIN e as demais requisições são permitidas para qualquer usuário autenticado (as requisições permitidas a todos estão em uma "white list", já que o padrão é que todas as requisições só podem ser feitas por usuários autenticados) .

Por fim chamaremos o método `addFilterBefore` para determinar que o filtro que criamos será chamado antes do filtro `UsernamePasswordAuthenticationFilter`

- O `UsernamePasswordAuthenticationFilter` é o filtro chamado no momento que o usuário faz login, recuperando username e senha e realizando a validação. Por padrão usuário só pode realizar as requisições após logado, por isso no nosso `SecurityFilter` setamos no contexto da aplicação que o usuário está autenticado após validarmos o JWT, porque de fato ele já está. Por isso posicionamos nosso filtro exatamente aqui na "corrente de filtros".

Posteriormente iremos criar os endPoints para login e registro de usuários.

Agora iremos criar outros beans que serão úteis posteriormente dentro da aplicação, o `authenticationManager` e o `PasswordEncoder`. 

```java
@Configuration  
@EnableWebSecurity  
@AllArgsConstructor  
public class SecurityConfig {  
  
    private final SecurityFilter securityFilter;  
  
  
    @Bean  
    public SecurityFilterChain securityFilterChain (HttpSecurity httpSecurity) throws Exception{  
        return httpSecurity  
                .csrf(csrf -> csrf.disable())  
                .sessionManagement(session ->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))  
                .authorizeHttpRequests(authorize -> authorize  
                        .requestMatchers(HttpMethod.POST,"/auth/login").permitAll()  
                        .requestMatchers(HttpMethod.POST,"/auth/register").permitAll()  
                        .requestMatchers(HttpMethod.POST,"/products").hasRole("ADMIN")  
                        .anyRequest().authenticated())  
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)  
                .build();  
    }  
    @Bean  
    public AuthenticationManager authenticationManager (AuthenticationConfiguration authenticationConfiguration) throws Exception {  
        return authenticationConfiguration.getAuthenticationManager();  
    }  
    @Bean  
    public PasswordEncoder passwordEncoder(){  
        return new BCryptPasswordEncoder();  
    }
}
```

Passo 8 ->

--- 

# Referências

