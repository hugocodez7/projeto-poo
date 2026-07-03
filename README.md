# 🍽️ MuhutiFood — Sistema de Pedidos para Restaurante

Projeto final da disciplina de **Programação Orientada a Objetos (POO)** do curso de **Análise e Desenvolvimento de Sistemas — IFPB Campus Monteiro**.

O **MuhutiFood** é um sistema desktop feito em **JavaFX**, com persistência em arquivos **JSON** usando **Gson**. O sistema permite configurar um restaurante, cadastrar clientes, gerenciar cardápio, realizar pedidos, acompanhar histórico e controlar o status dos pedidos pelo painel do gerente.

---

## 👥 Equipe

| Integrante                       | Matricula    |
|----------------------------------|--------------|
| Hugo Cesar de Lacerda Figueiredo | 202525020004 |
| Tiago Henrique Almeida Silva     | 202525020012 |
| Murilo Hiago Barreiro Neves      | 202525020003 |

---

## ✅ Pré-requisitos

- **Java Development Kit (JDK) 21** ou superior  
  Verifique com: `java -version`  
  Download: https://adoptium.net

- **Maven 3.8+** instalado e disponível no PATH  
  Verifique com: `mvn -version`  
  Download: https://maven.apache.org/download.cgi

---

## 🚀 Como Executar

### 1. Clone o repositório

```bash
git clone https://github.com/hugocodez7/projeto-poo.git
cd projeto-poo
```

---

### 2a. Executar pelo Terminal

```bash
mvn javafx:run
```

> Execute sempre a partir da **raiz do projeto** (onde está o `pom.xml`).

---

### 2b. Executar no Eclipse

1. **File → Import → Maven → Existing Maven Projects** → selecione a pasta do projeto → **Finish**
2. Aguarde o Eclipse baixar as dependências (barra de progresso no rodapé)
3. Clique com o botão direito no projeto → **Run As → Maven Build...** (com reticências)
4. No campo **Goals**, digite: `javafx:run`
5. Clique em **Run**

> Nas próximas execuções: botão direito no projeto → **Run As → Maven Build** (sem reticências).

---

### 2c. Executar no VS Code

1. Instale a extensão **Extension Pack for Java** (Microsoft)
2. **File → Open Folder** → selecione a pasta do projeto
3. Aguarde o VS Code indexar o projeto (barra de progresso no rodapé)
4. Abra o terminal integrado: **Terminal → New Terminal**
5. Execute:

```bash
mvn javafx:run
```

Alternativamente, use o painel **Maven** na barra lateral → expanda o projeto → **Plugins → javafx → javafx:run** (duplo clique).

---

## ✅ Tecnologias Utilizadas

- **Java 21**
- **JavaFX**
- **Maven**
- **Gson**
- **Json**
- **SceneBuilder**
- **IntelliJ IDEA**
- **Git e GitHub**


---

## 🧭 Fluxo Principal do Sistema

### Primeira execução

Na primeira vez que o sistema é aberto, ele verifica se já existe um restaurante configurado.
Se não existir, o gerente informa os dados do restaurante e cria o primeiro acesso de gerente.

Depois disso, o sistema passa a abrir diretamente a tela de login.

---

### Login

O sistema possui dois tipos de usuário:

```text
GERENTE
CLIENTE
```

Após o login:

- gerente entra no **Painel do Gerente**;
- cliente entra no **Cardápio**.

---

## 🧩 Funcionalidades

### Gerente

O gerente pode:

- configurar os dados do restaurante;
- acessar o painel administrativo;
- cadastrar itens no cardápio;
- editar itens;
- remover itens;
- importar cardápio por JSON;
- visualizar pedidos;
- filtrar pedidos por status;
- avançar o status dos pedidos.

### Cliente

O cliente pode:

- se cadastrar;
- fazer login;
- visualizar itens disponíveis do cardápio;
- adicionar itens ao carrinho;
- finalizar pedido;
- acompanhar histórico de pedidos;
- cancelar pedido enquanto ainda não foi confirmado.

---

## 📦 Estrutura do Projeto

```text
projeto-poo/
├── data/                       # Dados gerados em execução
├── exemplos-json/              # Arquivos de exemplo para importação
│   ├── cardapio_exemplo.json
│   └── imagens/
├── src/
│   └── main/
│       ├── java/
│       │   └── br.edu.ifpb.ads.foodjava/
│       │       ├── MainApp.java
│       │       ├── controller/
│       │       ├── exception/
│       │       ├── model/
│       │       ├── repository/
│       │       ├── service/
│       │       └── util/
│       └── resources/
│           ├── fxml/
│           └── images/
├── pom.xml
├── README.md
└── .gitignore
```

---

## 🗂️ Pacotes do Projeto

### `controller`

Contém os controladores das telas JavaFX. Os controllers fazem a ligação entre as telas FXML, os services e os repositories.

---

### `model`

Contém as classes principais do domínio do sistema. Essas classes representam os dados principais usados pelo sistema.

---

### `repository`

Contém as classes responsáveis por salvar e carregar dados em JSON. Os repositories evitam que os controllers acessem arquivos diretamente.

---

### `service`

Contém regras de negócio do sistema.

---

### `util`

Contém classes auxiliares usadas em várias partes do sistema. Essas classes ajudam a evitar repetição de código.

---

### `exception`

Contém exceções personalizadas do projeto. Elas deixam as regras de erro mais claras e organizadas.

---

## 💾 Persistência em JSON

O sistema usa arquivos JSON para salvar os dados.

A pasta `data/` é criada automaticamente em tempo de execução e armazena os dados reais do sistema:

```text
data/
├── cliente.json
├── gerente.json
├── restaurante.json
├── cardapio.json
└── pedidos.json
```

---

## 📁 Exemplo de Importação de Cardápio

O projeto possui um arquivo de exemplo:

```text
exemplos-json/cardapio_exemplo.json
```

Esse arquivo pode ser usado para testar a funcionalidade de importação de cardápio.


---

Projeto acadêmico — IFPB Campus Monteiro · 2026.1
