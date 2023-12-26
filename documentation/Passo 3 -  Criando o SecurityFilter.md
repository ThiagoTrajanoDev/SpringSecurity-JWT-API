
# Passo 3 -  Criando o SecurityFilter


Ao receber uma requisição HTTP, esta passa por um filtro, que verifica as permissões do usuário que enviou a requisição e faz o processamento correto, enviando um 200 se for aceito ou um 403 em caso se for negado (apenas a nível de permissão, outros status podem ser retornado em outros casos). 

Dessa forma precisamos criar e configurar a nossa classe filtro. Iremos chamá-la de `SecurityFilter`. Nosso `SecurityFilter` herda de `OncePerRequestFilter`, cujo nome é auto explicativo, a cada requisição feita, há uma garantia que uma única execução da nossa classe irá ser realizada. Nossa classe base nos obriga a implementar um método chamado de `doFilterInternal()`, que recebe um `HttpServletRequest`, uma `HttpServletResponse` e um `FilterChain`, o primeiro nos permite interceptar a requisição e extrair seus dados, o segundo nos permite alterar a resposta enviada, podemos acessar os headers e os cookies do HTTP através de seus métodos por exemplo, e o último é a representação do design pattern chamado de  _Chain of Responsabily_, que encadeia filtros de forma sequencial.  


Antes de implementar o `doFilterInternal`, iremos criar uma função auxiliar privada que retorna um token JWT presente no header caso haja algum. Basicamente, caso nossa requisição possua algum header chamado de _Authorization_ iremos recuperá-lo, caso não ou caso o nosso header não comece com "_Bearer_ " (importante o espaço após o termo) retornamos _null_.  O _Bearer_ é o nome dado ao nosso token e ele está dentro do nosso header normalmente no formato:
	`Authentication: Bearer <token>`
Dessa forma se substituímos a palavra chave por uma string vazia, iremos obter apenas o nosso token (também poderíamos fazer uma substring).

``` java
private String recoveryToken(HttpServletRequest request) {  
    String authHeader = request.getHeader("Authorization");  
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {  
        return null;  
    }    return authHeader.replace("Bearer ", "");  
}
```

Após isso, podemos seguir com a implementação do método `doFilterInternal`. Chamamos a nossa função para recuperar o token JWT, caso haja algum iremos fazer a validação do mesmo usando um Service JWT e extrair o nome de usuário, o que nos leva ao passo seguinte.

Passo 4 ->

--- 

# Referências

