# Patrimônio Intangível

Esta aplicação um módulo do SIGA Patrimônio, 
que tem por objetivo gerenciar todo o histórico de vida de um bem intangível.

Bem Intangível enquadra todos os bens não monetários, sem substância física, identificáveis, 
controlados pela entidade e geradores de benefícios econômicos futuros ou potencial de serviços.

## Como desenvolver

Para iniciar o desenvolvimento neste sistema espera-se que seu ambiente já esteja configurado segundo os procedimentos descritos no [manual de configuração do hal](http://git.azi.com.br/hal/ambiente).

#### Antes de começar

Após a configuração do seu ambiente vamos instalar um pacote utilitário feito em nodejs para nos auxiliar com os comandos necessários durante o desenvolvimento.

O **hal-cli** é um pacote criado para facilitar nosso fluxo de desenvolvimento. Ele irá prover comandos que te ajudará a subir os container docker, atualizar configurações de rotas, atualizar o número da versão do projeto e muito mais.

Instale o hal-cli globalmente

```bash
npm i hal-cli -g
```

#### PASSO 1 - Execute o comando abaixo na pasta raiz do repositório
Este comando irá subir todas as dependências do docker que esta aplicação depente para ser executado.

```bash
hal up
```

#### PASSO 2 - Suba a Api pela IDE
Para subir a API, utilize a funcionalidade *Run Configurations* do IntelliJ.
Na criação da Run Configuration aponte para a classe principal do projeto `patrimonio-intangivel-api-application`.
Em seguida, copie as variáveis de ambiente abaixo para o seu Run Configuration.

```bash
DATABASE_URL=jdbc:postgresql://localhost:5432/az
DATABASE_CLASSNAME=org.postgresql.Driver
DATABASE_USERNAME=postgres
DATABASE_PASSWORD=postgres
DATABASE_PLATFORM=org.hibernate.dialect.PostgreSQLDialect
DATABASE_VALIDATION_QUERY=SELECT 1
LIQUIBASE_CONTEXTS=desenv
HAL_CONFIG_URL=http://localhost:8000
HAL_DISCOVERY_URL=http://localhost:8001
CONFIG_URL=http://localhost/hal-config/settings
EFORNECEDOR_URL=http://localhost/efornecedor/efcaz-api
HAL_URL=http://localhost/setup/hal
```

#### PASSO 3 - Suba o frontend pela IDE
Basta entrar na subpasta do projeto frontend e executar o comando
```bash
npm run serve
```

#### PASSO 4 - Atualize o gateway com o seu IP
Na pasta raiz do produto execute o comando abaixo para o gateway saber redirecionar para a sua máquina quando acessar o frontend.
```bash
hal update-gateway
```

#### PASSO 5 - Acesse o projeto
Pronto, basta acessar: http://localhost/patrimonio-intangivel/

## Usuários em ambiente de desenvolvimento

Em ambiente de desenvolvimento nós temos alguns usuários que irão nos ajudar durante o desenvolvimento e teste

**Admin** usuário com acesso administrativo e com todas as permissões
```bash
login: admin
senha: 123
```

**Servidor** usuário com acesso de edição nos órgãos
```bash
login: servidor
senha: 123
```

**Consultor** usuário com acesso de consulta nos órgãos
```bash
login: consultor
senha: 123
```

## Iniciando com banco de dados específico

Neste sistema damos suporte para 3 diferentes banco de dados **oracle**, **postgres**, **sql server**. Por este motivo devemos testar o sistema em todos os bancos de dados durante o desenvolvimento.

Na configuração de iniciação do sistema está configurado como padrão o banco da dados postgres, então quando vamos iniciar o sistema com outro banco de dados, precisamos de algumas configurações especiais.

#### PASSO 1 - Alterado
No passo 2 do manual de configuração do sistema, vamos rodar um comando específico para cada banco

**Oracle**
```bash
hal up --database mongo,oracle
```

**Sql Server**
```bash
hal up --database mongo,mssql
```

#### PASSO 2 - Alterado
No passo 3 onde configuramos nossas variáveis de ambiente na IDE vamos alterar as variáveis referente ao banco de dados as outras permanecem as mesmas.

**Oracle**
```bash
DATABASE_URL=jdbc:oracle:thin:@localhost:1521:XE
DATABASE_CLASSNAME=oracle.jdbc.OracleDriver
DATABASE_PLATFORM=org.hibernate.dialect.Oracle12cDialect
DATABASE_USERNAME=system
DATABASE_PASSWORD=oracle
DATABASE_VALIDATION_QUERY=SELECT 1 FROM DUAL
```

**Sql Server**
```bash
DATABASE_URL=jdbc:sqlserver://localhost:1433;databaseName=az;
DATABASE_CLASSNAME=com.microsoft.sqlserver.jdbc.SQLServerDriver
DATABASE_PLATFORM=org.hibernate.dialect.SQLServer2012Dialect
DATABASE_USERNAME=sa
DATABASE_PASSWORD=S3nha@123
```

-----
© Copyright 2020 - All rights reserved | Todos os direitos Reservados

__AZ Tecnologia em Gestão__
