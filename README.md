## Autenticação e autorização com Spring Security e JWT

Esse tutorial tem como objetivo explicar como realizar uma configuração simples para  autenticação e autorização da sua API.


#### A API utilizada

A API é simples e não entrarei em detalhes sobre a implementação dela aqui, é uma simples API de produtos, simulando um e-commerce, em que os produtos tem um código único, um nome, um preço e uma quantidade. A API disponibiliza de CRUD completo.

#### O que será implementado

Utilizaremos o Spring Security para armazenar os dados do usuário em um banco de dados em memória, realizando uma encriptação da senha antes disso. 

Os usuários com permissão de ADMIN poderão cadastrar, apagar  e atualizar os produtos, os usuários comuns poderão buscar os produtos, utilizando a API como readOnly


#### Conceitos importantes

Para realizar a proteção dos nosso endPoints iremos utilizar o JWT. JWT é uma sigla para Json Web Token, esse é um padrão aberto que define uma forma segura de transmitir informação entre duas partes. 

O JWT é formado por 3 partes: O header, o payload, e a assinatura (signature). Seu formato é dado por: 
	`xxxxxx.yyyyyy.zzzzzz`

O header, normalmente, é formado por duas partes: o tipo do token (JWT) e o tipo do algoritmo usado para criptografia da assintura.

O payload é a parte que contém o que chamamos de _Claims_, que são declarações sobre uma entidade, (normalmente o usuário) e alguns outros dados. Existem 3 tipos de claims: registerd, public e private. Nessa aplicação usaremos explicitamente apenas os registered.

E a assinatura é a união do header e do payload já codificados e um segredo, tudo isso criptografado com o algoritmo de criptografia descrito no header. 

#### Como o uso do JWT irá funcionar?

Ao realizar login com sucesso, iremos retornar um JWT ao cliente em uma resposta HTTP 200. Sempre o usuário necessitar realizar alguma requisição HTTP, iremos receber o token ativo, extrair os login do usuário a partir dele e verificar se este possui as permissões necessárias e liberar ou não a requisição.


#### Estrutura

O guia será estruturado nos seguintes passos: 

- Passo 1 - Criando o domínio da aplicação
- Passo 2 - Criando o UserRepository
- Passo 3 - Criando o SecurityFilter
- Passo 4 - Criando o TokenService ou JwtService
- Passo 4 - Criando o TokenService ou JwtService
- Passo 5 - Criando o AuthorizationService
- Passo 6 - Finalizando o SecurityFilter
- Passo 7 - Criando o SecurityConfig
- Passo 8 - Criando o AuthenticationController

