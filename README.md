 <p align="center">
  <img src="./frontend/src/assets/icon.svg" alt="HidroLab Logo" width="80" style="vertical-align: middle;"/>
  <span style="font-size: 1.8em; font-weight: bold; vertical-align: middle;"> HidroLab - Análise de Vazões e Ferramentas Hidrológicas</span>
</p>


# Sobre o projeto

**HidroLab** é uma plataforma interativa desenvolvida para facilitar a análise hidrológica com base nos dados de vazão das estações monitoradas pela Agência Nacional de Águas e Saneamento Básico (ANA). A aplicação foi pensada para pesquisadores, profissionais da área ambiental e gestores de recursos hídricos que necessitam de ferramentas práticas e atualizadas para visualizar e interpretar séries históricas de vazão.

## Funcionalidades:

* Banco de dados local pré-carregado com todas as séries históricas de vazão disponíveis na API da ANA, atualizadas até 01/06/2025.
* Verificação automática de conectividade e atualização de dados: ao buscar uma estação específica, o sistema verifica a disponibilidade de novos dados na API da ANA e os insere automaticamente no banco da aplicação, caso existam.
* Visualização gráfica da Curva de Permanência, com suporte às versões empírica e logarítmica.
* Cálculo automático dos principais Q-valores: Q90, Q95, Q98 e Q50.
* Geração de estatísticas por classe de vazão, permitindo análises comparativas e de distribuição.
* Gráficos de frequência acumulada para análise da distribuição das vazões.
* Filtros por intervalo de datas, oferecendo maior controle sobre o período analisado.
* Opção de visualização dos dados em granularidade diária ou mensal.

## Técnologias utilizadas:

* Front-end: React + TypeScript
* Back-end: Spring Boot (Java)
* Banco de dados: PostgreSQL 

---

## Arquitetura geral:

![image](https://github.com/user-attachments/assets/789b567d-9e43-415d-83ec-0a7d43d39fcd)


---

# Subindo a aplicação com Docker Compose

**Pré-requisitos**: Docker e Docker Compose instalados na máquina.

1. Faça o download da pasta ;
2. Abra um terminal dentro da pasta `hidrolab` disponível para download [aqui](https://drive.google.com/drive/folders/1UoGwCXeufHPbtGOYPKU783534Q_fMWoZ?usp=sharing);
3. Execute o comando: `docker-compose up -d`;

A aplicação estará disponível em: [http://localhost:8081/](http://localhost:8081/)

Para fins de teste, o código de uma estação que pode ser utilizado é: `12500000`;

## Instalação (Back-End com Spring Boot)

### 1. Requisitos

* [Java JDK 17+](https://adoptium.net)
* [Maven](https://maven.apache.org/) ou extensão "Java" no VS Code
* [PostgreSQL](https://www.postgresql.org/)
* [VS Code](https://code.visualstudio.com/) ou IntelliJ

### 2. Clonar o repositório

```bash
git clone https://github.com/andrehtp/FlowCalc.git
cd FlowCalc
```

### 3. Configurar o `application.properties`

Crie variáveis ambientes ou edite o arquivo em `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/flowcalc
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha

spring.jpa.hibernate.ddl-auto=none
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true


# CAPTCHA (ex: Google reCAPTCHA)
app.captcha.secret=SUA_CHAVE_DO_RECAPTCHA
```

> ⚠️ Nunca suba essas credenciais em repositórios públicos.

### 4. Executar a aplicação

Você pode:

* Rodar via terminal com Maven:

```bash
./mvnw spring-boot:run
```

* Ou abrir o projeto no VS Code, clicar com o botão direito no `ProjIntegradorApplication.java` e selecionar **Run Java** (após instalar a extensão "Java Extension Pack").

Aplicação estará disponível em `http://localhost:8080`

---

## Instalação (Front-End com React)

### 1. Requisitos

* [Node.js](https://nodejs.org/) (v18+)

### 2. Instalar dependências

```bash
cd frontend
npm install
```

### 3. Rodar localmente

```bash
npm run dev
```

Frontend disponível em `http://localhost:5173`

---


---
## Licença

Licença MIT. Veja [LICENSE](./LICENSE).

---

## Contato

Desenvolvido por 
             André Tavares      - [LinkedIn](https://www.linkedin.com/in/andrehtavares)
            |
            Jean Pelissoli     - [LinkedIn](https://www.linkedin.com/in/jeanpelissoli/) 
            | 
            Cassiano Luzzietti - [LinkedIn](https://www.linkedin.com/in/cassiano-matias-luzzietti-a4871421b/)

---


