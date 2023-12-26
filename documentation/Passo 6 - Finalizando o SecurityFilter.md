
# Passo 6 - Finalizando o SecurityFilter

No passo 4 criamos o TokenService que nos permite validar um token recebido, extraindo o nome de usuário caso o token seja válido, e no passo 5 criamos o AuthorizationService que possui um método que recupera o usuário do banco, dado seu login.

A partir disso podemos dar continuidade a classe SecurityFilter (que é invocada após o recebimento de uma requisição HTTP e filtrar se a requisição é devida).

Após recuperar o token é necessário realizar uma validação deste e recuperaremos o login. Caso o login seja nulo a função `loadUserByUsername` retorna uma exceção `UsernameNotFoundException`, caso não a função irá recuperar o `UserDetails` a partir do login. Após isso iremos criar uma instância de `UsernamePasswordAuthenticationToken` e atualizar o contexto da nossa aplicação.

Terminado isso, chamamos o `filterChain.doFilter(request,response)` para continuar com o restantes dos filtros.

```java
@Component  
@AllArgsConstructor  
public class SecurityFilter extends OncePerRequestFilter {  
  
    private final TokenService tokenService;  
    private final AuthorizationService authorizationService;  
  
    @Override  
    protected void doFilterInternal(HttpServletRequest request,  
                                    HttpServletResponse response,  
                                    FilterChain filterChain) throws ServletException, IOException {  
        String token = this.recoveryToken(request);  
        if (token != null) {  
            String username = tokenService.validateToken(token);  
            UserDetails user  = authorizationService.loadUserByUsername(username);  
  
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user,null, user.getAuthorities());  
            SecurityContextHolder.getContext().setAuthentication(authentication);  
        }  
              
        filterChain.doFilter(request,response);  
    }  


    private String recoveryToken(HttpServletRequest request) {  
        String authHeader = request.getHeader("Authorization");  
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {  
            return null;  
        }        return authHeader.replace("Bearer ", "");  
    }
}
```


--- 

# Referências

[SecurityContext](https://www.javacodegeeks.com/2018/02/securitycontext-securitycontextholder-spring-security.html)
