# Pasta de Imagens — Exemplos de Cardápio

Esta pasta deve conter as imagens referenciadas no arquivo `cardapio_exemplo.json`.

## Imagens esperadas pelo JSON de exemplo

| Arquivo | Item do cardápio |
|---------|-----------------|
| `caldo_feijao.jpg` | Caldo de Feijão |
| `macaxeira_frita.jpg` | Macaxeira Frita |
| `baiao_de_dois.jpg` | Baião de Dois |
| `carne_de_sol.jpg` | Carne de Sol na Chapa |
| `frango_assado.jpg` | Frango Assado Caipira |
| `munguzza.jpg` | Mungunzá |
| `pudim.jpg` | Pudim de Leite Condensado |
| `suco_umbu.jpg` | Suco de Umbu |

## Observações

- Itens sem `imagemPath` no JSON ou cujo arquivo não seja encontrado devem exibir uma imagem padrão (placeholder) na interface.
- Formatos aceitos: **JPG** e **PNG**.
- Tamanho máximo: **2 MB** por imagem.
- O caminho no JSON deve ser **relativo à raiz do projeto**.

## Como testar

1. Coloque imagens reais nesta pasta com os nomes listados acima.
2. No sistema, acesse o painel do gerente → Cardápio → Importar JSON.
3. Selecione o arquivo `cardapio_exemplo.json`.
4. Os itens serão importados e as imagens exibidas no cardápio do cliente.
