
# Passo 8 - Criando o AuthenticationController



Criar o controller é bastante simples. Anotamos com `@RestController`, passamos o endpoint "/auth" para `@RequestMapping`, injetamos o `TokenService`,`AuthenticationManeger` , `UserRepository` e `passwordEncoder` e criamos um `@PostMapping` para "/login" e outro para "/register".

Para realizar o login, criamos um método que retorna uma `ResponseEntity` e recebe uma AuthDTO (que é apenas um record que contém login e senha). Criamos um objeto do tipo `UsernamePasswordAuthenticationToken` passando login e senha recebidos e passamos esse objeto para o método `authenticate` do `AuthenticationManeger`, isso garante uma fácil verificação do usuário e retorna um objeto `Authentication`, que armazena todos os dados do usuário realizando o login. Geramos um token utilizando o nosso `TokenService` e depois disso retornamos esse token ao usuário em uma resposta HTTP.

Para registrar o usuário, primeiro verificamos se o login já existe, caso sim retornamos uma exceção. Caso não, recuperamos a senha que o usuário enviou e criamos um hash a partir dela utilizando `passwordEncoder` que criamos na nossa classe de configuração. Pegamos o nome de usuário, senha encriptada  e permissão do usuário e criamos uma instância de User e salvamos no banco de dados. Retornamos uma 200 ao usuário.

```java
@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthenticationController {

    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @PostMapping("/login")
    public ResponseEntity login(@RequestBody AuthDTO authDTO){
        UsernamePasswordAuthenticationToken userNamePassword = new UsernamePasswordAuthenticationToken(authDTO.login(), authDTO.password());
        Authentication auth = authenticationManager.authenticate(userNamePassword);
        String token =  tokenService.generateToken((User) auth.getPrincipal());
        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody RegisterDTO registerDTO) {
        if(this.userRepository.findByLogin(registerDTO.login()) != null)  {
            return ResponseEntity.badRequest().build();
        }
        String encryptedPassword = passwordEncoder.encode(registerDTO.password());
        User user =  new User(registerDTO.login(),encryptedPassword, registerDTO.role());
        this.userRepository.save(user);

        return ResponseEntity.ok().build();
    }
}
```

--- 

# Referências

