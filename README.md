Mapa Universitário - Web
===================
O Mapa Univeristário é um projeto web que permite que um usuário autenticado no Facebook possa criar marcações no Google Maps customizadas sobre vagas em repúblicas e moradias de estudantes universitários. Qualquer pessoa pode visualizar a localização no mapa e ainda pode ver detalhes como preço e telefone para contato. Somente usuários autenticados no Facebook podem adicionar, alterar e excluir vagas no mapa.

Apresentação do sistema
===================
A tela principal do sistema exibe uma barra superior em azul contendo um ícone para fazer login no facebook e um ícone para ocultar o menu lateral. O login no facebook também pode ser feito através do link no menu lateral. O menu também exibe a lista de moradias cadastradas no mapa. Para dar um zoom em uma moradia basta clicar sobre ela no menu. Na parte central da página é exibido o mapa do Google e todas as moradias cadastradas são plotadas no mapa.

![Mapa Universitário](/src/main/webapp/resources/mu/img/mu01.png)

Ao clicar sobre um ponto no mapa, é possível visualizar todos os detalhes do ponto cadastrado.

![Mapa Universitário](/src/main/webapp/resources/mu/img/mu03.png)

Quando o usuário faz o login no facebook (pelo link do menu lateral, ou na barra superior), a função cadastrar aparece na barra superior (ícone "+") e com isso é possível adicionar uma nova moradia no mapa.

![Mapa Universitário](/src/main/webapp/resources/mu/img/mu09.png)

O usuário também pode remover ou alterar alguma informação já cadastrada. Basta clicar sobre o item no mapa e usar os botões atualizar ou remover.

![Mapa Universitário](/src/main/webapp/resources/mu/img/mu011.png)

Instalação
===================
O projeto foi desenvolvido utilizando:

- Eclipse Neon
- Java 8
- Apache Maven
- Java Server faces
- WELD (CDI)
- SocialAuth - Responsável pela integração com a API do Facebook
- Toda a camada de persistência de dados foi desenvolvida em um projeto Spring RESTFul que se encontra nesse repositório git: [MapaUniversitarioAPI](https://github.com/rafaelbss/MapaUniversitarioAPI)

Pra configurar o projeto basta clonar ou fazer download do projeto: [https://github.com/rafaelbss/MapaUniversitarioWeb.git](https://github.com/rafaelbss/MapaUniversitarioWeb.git).

Para importar o projeto no Eclipse(Java EE) acesse:

File > Import > Maven > Existing maven projects.

Depois selecione o diretório onde o projeto foi baixado marque o projeto e clique em Finish.

Depois clique sobre o projeto > Maven > Update project.

Em seguida o maven irá baixar as dependências.

Estrutura do projeto
===================

![Mapa Universitário](/src/main/webapp/resources/mu/img/mu012.png)

O pacote src/main/java/app/web.mu.business.facade contém as classes de negócio que comunicam com os métodos da API RESTFul e expõe esses métodos para a camada de controle da aplicação.

O pacote src/main/java/app/web.mu.business.model representa todas as entidades do sistema.

O pacote src/main/java/app/web.mu.business.model.util contém a classe SocialUtil responsável por realizar a integração com a API do Facebook.

O pacote src/main/java/app/web.mu.config representa todas as classes de configuração do projeto e a classe que realiza a comunicação com a API RESTFul [MapaUniversitarioAPI](https://github.com/rafaelbss/MapaUniversitarioAPI).

O pacote src/main/java/app/web.mu.controller.bean representa todos os managed beans que interagem com as telas do sistema web.

O pacote src/main/java/app/web.mu.jsf.util contém as classes que lidam com as exceções ocorridas durante o ciclo de vida da aplicação JSF.

A pasta src/main/resources contém os arquivos de internacionalização do sistema e a configuração do log4j.

A pasta src/main/webapp contém os arquivos xhtml do padrão facelets, css, javascript e arquivos de imagem da aplicaço.

A pasta src/main/webapp/WEB-INF/template contém os templates de layout da aplicação.

A pasta doc contém todo o JavaDoc do projeto.
