# play-framework-backend

API backend construida utilizando o play framework em java inicialmente para requisições REST do projeto [carros-angular-frontend](https://github.com/alephlm/carros-angular-frontend). Esse backend tem como base o [exemplo](https://github.com/playframework/play-scala-rest-api-example) dado pela pagina do play frameork.  

Esse backend tem como principal atividade controlar as solicitações de criação, listagem, atualização e remoção de carros.

## Inicialização
Depois de clonado o repositório, deve-se entrar na basta e executar o comando:
```shell
./sbt run
```
isso irá executar e gerar todos os arquivos necessarios. Caso queira acessar diretamente o backent atraves de chamadas html, o projeto ficará disponivel na porta 9000 do localhost.  

Esse projeto utiliza o banco de dados em memória H2. Ou seja, quando o backend for reiniciado, todos os dados serão perdidos.

## Recurrsos utilizados
Play framework - https://www.playframework.com
