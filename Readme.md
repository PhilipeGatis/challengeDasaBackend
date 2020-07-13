## challengeDasaBackend

###### Sistem de manutenção de exames e laboratórios
Demo: https://dasachallengebackend.herokuapp.com/

#### Requisitos
 - java 8
 - maven
 - [pre-commit](https://pre-commit.com/) (seguir documentação de instalação)
 - docker
 - postgres

#### Arquitetura
 - spring
 - lombok
 - flyway
 - swagger
 - junit

###### Atenção: todos os comando a seguir são rodadados no terminal na pasta do projeto

#### Instruçoes docker:
###### Todas as configuraçoes dos serviços do docker estão no arquivo **[docker-compose.yml](/docker-compose.yml)**
###### A flag ```-d``` significa para o container rodar em background.
###### Para mais informações de uso acesse [docker-compose](https://docs.docker.com/compose/reference/overview/).
 - Banco de dados:

    Rodar o comando ```docker-compose up -d postgres``` que irá o banco de dados em background.

 - Aplicação:

    Rodar o comando ```docker-compose up -d --build app``` que irá subir a applicação. Para a aplicação subir é necessário ter o banco rodando e ter gerado o jar da aplicação.

 - Comandos uteis:
    - ```docker-compose stop``` irá parar todos os serviços
    - ```docker-compose logs -f``` logs de todos os serviços
    - ```docker-compose stop postgres``` irá parar o banco
    - ```docker-compose logs -f postgres``` log do banco
    - ```docker-compose stop app``` irá parar a aplicação
    - ```docker-compose logs -f app``` log da aplicação

#### Instruções de build
###### Atenção: os testes da aplicação necesita do banco de dados com um schema limpo criado
 - Gerando o jar da aplicação:

    Rodar o comando ```mvn clean package -DskipTests```, este comando irá buildar o sistema pulando a fase de testes unitários, para rodar com os testes remover o ```-DskipTests``` do comando.
    A aplicação(jar) gerada terá as configurações que estavam no arquivo [application.properties](/src/main/resources/application.properties)

 - Testando:

    Rodar o comando ```mvn test```, este comando irá rodar os testes da aplicação com a configuração de banco salvo no arquivo [application.properties](/src/test/resources/application.properties).


#### Acesso
  Após ter o APP rodando acessar o serviço em ```http://localhost:8080/``` esse endereço irá disponibilizar a documentação no swagger.
