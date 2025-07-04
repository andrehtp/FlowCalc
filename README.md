 <p align="center">
  <img src="./frontend/src/assets/icon.svg" alt="HidroLab Logo" width="80" style="vertical-align: middle;"/>
  <span style="font-size: 1.8em; font-weight: bold; vertical-align: middle;"> HidroLab - Análise de Vazões e Ferramentas Hidrológicas</span>
</p>


## Sobre o projeto

**HidroLab** é uma plataforma interativa para análise hidrológica com base em dados de vazão de estações do governo brasileiro (ANA).

Ele permite:
* Extrai automaticamente de forma mensal os dados públicos do site da ANA (Agência Nacional de Águas e Saneamento Básico). 
* Tratamamento e armazenamento local dos dados extraído em um próprio banco de dados.
* Curva de Permanência (Empírica e Logarítmica)
* Cálculo de Q-valores (Q90, Q95, Q98, Q50)
* Estatísticas por classe de vazão
* Gráficos de frequência acumulada
* Filtro por intervalo de datas
* Seleção entre vazões diárias ou mensais

Arquitetura do sistema:

* Front-end: React + TypeScript
* Back-end: Spring Boot (Java)
* Banco de dados: PostgreSQL (local ou na nuvem)

---

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

## Funcionalidades atuais

* Integração com API da ANA (SNIRH)
* Mapa interativo com estações
* Filtros por região, tipo de estação e período
* Cálculo de curva de permanência

---

## Funcionalidades futuras

* Exportação para .csv
* Geração de relatório em .pdf
* Cálculos de mínimas (Q7,10)

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


