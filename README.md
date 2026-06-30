# FoodJava

Sistema de gerenciamento de pedidos para restaurante, desenvolvido como projeto final da disciplina de Programação Orientada a Objetos — IFPB Campus Monteiro.

## Integrantes

- Hugo
- Tiago
- Murilo

## Pré-requisitos

- Java 17 ou superior
- Maven (já configurado via wrapper do projeto)

Para verificar se o Java está instalado corretamente:

```bash
java -version
```

## Como executar

Clone o repositório:

```bash
git clone https://github.com/hugocodez7/projeto-poo.git
cd projeto-poo
```

Execute o sistema:

```bash
mvn javafx:run
```

Na primeira execução, o sistema abre automaticamente a tela de **Configuração Inicial do Restaurante**, onde o gerente deve cadastrar os dados do estabelecimento. Nas execuções seguintes, o sistema abre diretamente na tela de **Login**.

## Funcionalidades

### Perfil Gerente
- Configuração inicial do restaurante
- Gerenciamento do cardápio (cadastro, edição, exclusão e importação via JSON)
- Visualização e avanço de status dos pedidos
- Painel com resumo do dia (total de pedidos e faturamento)
- Edição dos dados do restaurante

### Perfil Cliente
- Auto-cadastro e login
- Navegação pelo cardápio por categoria
- Montagem de carrinho e confirmação de pedido
- Acompanhamento do status do pedido em tempo real

## Importação de cardápio via JSON

O gerente pode importar itens do cardápio através de um arquivo JSON seguindo o padrão disponível na pasta [`/exemplos-json`](./exemplos-json). Categorias válidas: `ENTRADA`, `PRATO_PRINCIPAL`, `SOBREMESA`, `BEBIDAS`.

## Arquitetura

O projeto segue o padrão **MVC** com camada de **Repository**, organizado nos seguintes pacotes:

```
model/       - entidades do domínio (Usuario, Cliente, Gerente, Restaurante, Pedido, ItemCardapio...)
view/        - telas JavaFX
controller/  - controladores das telas
repository/  - persistência em arquivos JSON via Gson
exception/   - exceções customizadas do sistema
util/        - validadores, sessão e utilitários gerais
```

Os dados são persistidos em arquivos JSON na pasta `src/main/resources/data/`, gerados automaticamente em tempo de execução.

## Tecnologias utilizadas

- Java 17
- JavaFX
- Gson (persistência em JSON)
- Maven
