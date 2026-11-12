# 🚗 ParkControl — Sistema de Controle de Estacionamento

![Java](https://img.shields.io/badge/Java-11%2B-orange?logo=openjdk&logoColor=white)
![Swing](https://img.shields.io/badge/Interface-Java%20Swing-464A9E)
![MVC](https://img.shields.io/badge/Arquitetura-MVC-FF5B35)
![Armazenamento](https://img.shields.io/badge/Dados-Em%20mem%C3%B3ria-lightgrey)

**Disciplina:** Iniciação à Programação
**Grupo:** 08 — Sistema de Controle de Estacionamento

## 👥 Integrantes

| Nome | Responsabilidade principal |
|---|---|
| Artur Moisés Moraes Sousa | Model e regras de negócio (`Veiculo`, `Estacionamento`) |
| Gabriel Henrique da Silva Brandão | Controller e validações (`EstacionamentoController`) |
| Guilherme Araujo Silva | Telas de entrada e consulta (`TelaEntrada`, `TelaConsulta`) |
| João Pedro Gonsalves de Aquino | Tela de saída e cálculo de cobrança (`TelaSaida`) |
| João Victor Carvalho de Faris | Tela principal, login e tema visual (`TelaPrincipal`, `TelaLogin`, `Tema`) |
| Vinicius Medeiros de Carvalho Santos | Relatório operacional, testes e documentação (`TelaRelatorio`, README) |

---

## 📑 Sumário

1. [Sobre o projeto](#1-sobre-o-projeto)
2. [Funcionalidades](#2-funcionalidades)
3. [Acesso ao sistema](#3-acesso-ao-sistema)
4. [Regras de negócio](#4-regras-de-negócio)
5. [Arquitetura (MVC)](#5-arquitetura-mvc)
6. [Estrutura do projeto](#6-estrutura-do-projeto)
7. [Componentes Swing utilizados](#7-componentes-swing-utilizados)
8. [Requisitos do trabalho × atendimento](#8-requisitos-do-trabalho--atendimento)
9. [Como executar](#9-como-executar)
10. [Como usar o sistema](#10-como-usar-o-sistema)
11. [Roteiro de testes](#11-roteiro-de-testes)
12. [Como personalizar](#12-como-personalizar)
13. [Limitações conhecidas](#13-limitações-conhecidas)
14. [Possíveis melhorias futuras](#14-possíveis-melhorias-futuras)
15. [Tecnologias](#15-tecnologias)

---

## 1. Sobre o projeto

O **ParkControl** é uma aplicação desktop para controlar a **entrada e a saída de veículos**, a **ocupação das vagas** e os **valores cobrados** em um estacionamento.

O sistema é de uso **interno**: apenas **gerência** e **funcionários** têm acesso. O cliente do estacionamento não interage com a aplicação.

Foi desenvolvido em **Java puro (Java SE)**, com interface gráfica em **Java Swing**, arquitetura **MVC** e armazenamento de dados **somente em memória** (`ArrayList`).

---

## 2. Funcionalidades

| Nº | Funcionalidade | Onde está |
|---|---|---|
| 1 | **Registro de entrada** — cadastra placa, modelo, data e horário de entrada | Tela de Entrada |
| 2 | **Listagem de veículos** — exibe os veículos estacionados em uma `JTable` | Tela de Consulta |
| 3 | **Pesquisa por placa** — localiza veículos (pela placa inteira ou parte dela) e mostra a situação (Estacionado ou Saiu) | Tela de Consulta |
| 4 | **Registro de saída** — informa data e horário de saída e encerra a permanência | Tela de Saída |
| 5 | **Cálculo de permanência** — calcula o tempo total estacionado (horas e minutos) | Tela de Saída |
| 6 | **Cálculo de cobrança** — valor devido pela tarifa de **R$ 5,00 por hora ou fração**, com mínimo de 1 hora | Tela de Saída |
| 7 | **Relatório operacional** — veículos estacionados, vagas disponíveis e faturamento acumulado | Tela de Relatório (somente gerência) |

### Funcionalidades adicionais

- **Login com dois perfis** (Gerência e Funcionário), pois o sistema é restrito à equipe.
- **Relatório restrito à gerência**, porque mostra o faturamento.
- **Prévia da cobrança** antes de confirmar a saída.
- **Seleção do veículo pela tabela** na tela de saída (clique na linha para preencher a placa).
- Botão **"Usar data/hora atual"** nos formulários.
- **Resumo na tela principal** com veículos estacionados, vagas disponíveis e tarifa.

---

## 3. Acesso ao sistema

| Perfil | Senha | Permissões |
|---|---|---|
| **Gerência** | `gerente123` | Entrada, consulta, saída e **relatório operacional** |
| **Funcionário** | `func123` | Entrada, consulta e saída (relatório bloqueado) |

As senhas ficam no arquivo `src/model/Perfil.java` e podem ser alteradas.

> **Observação:** as senhas são fixas no código porque o trabalho não permite banco de dados nem armazenamento externo. Em um sistema real, elas ficariam criptografadas em um banco.

---

## 4. Regras de negócio

- **Capacidade:** 20 vagas (constante `CAPACIDADE` em `Estacionamento.java`).
- **Tarifa:** R$ 5,00 por hora ou fração (constante `TARIFA_POR_HORA` em `Estacionamento.java`).
- **Cobrança:** o tempo é arredondado **para cima** em horas inteiras. O mínimo cobrado é 1 hora.

| Permanência | Horas cobradas | Valor |
|---|---|---|
| 10 min | 1 | R$ 5,00 |
| 1h 00min | 1 | R$ 5,00 |
| 1h 01min | 2 | R$ 10,00 |
| 2h 10min | 3 | R$ 15,00 |

- **Placa:** aceita o formato antigo (`ABC1234`) e o Mercosul (`ABC1D23`). Letras minúsculas, espaços e hífen são normalizados (`abc-1234` vira `ABC1234`).
- **Não é permitido:**
  - registrar entrada de uma placa que já está estacionada;
  - registrar entrada com o estacionamento lotado;
  - registrar saída de um veículo que não está estacionado;
  - registrar saída com data/hora anterior à entrada;
  - informar modelo vazio, placa inválida ou data/hora inválida.
- **Faturamento:** soma de todos os valores cobrados nas saídas confirmadas.

---

## 5. Arquitetura (MVC)

```
┌──────────┐   eventos    ┌──────────────┐   operações   ┌───────────┐
│   VIEW   │ ───────────► │  CONTROLLER  │ ────────────► │   MODEL   │
│  (Swing) │ ◄─────────── │              │ ◄──────────── │ (regras + │
└──────────┘  dados/erros └──────────────┘   resultados  │ ArrayList)│
                                                          └───────────┘
```

| Camada | Responsabilidade | Classes |
|---|---|---|
| **Model** | Dados e regras de negócio: classes Java, validações de regra, cálculos e coleção em memória | `Veiculo`, `Estacionamento`, `Perfil` |
| **View** | Telas Swing: exibe informações e recebe as ações do usuário. **Não contém regra de negócio** | `TelaLogin`, `TelaPrincipal`, `TelaEntrada`, `TelaConsulta`, `TelaSaida`, `TelaRelatorio`, `TelaBase`, `TabelaVeiculos`, `Tema`, `PainelHero` |
| **Controller** | Recebe os eventos da View, valida e converte os dados digitados (placa, data, hora) e chama o Model | `EstacionamentoController` |

### Fluxo de exemplo: registrar uma entrada

1. O usuário preenche o formulário em `TelaEntrada` e clica em **Registrar entrada**.
2. A View chama `controller.registrarEntrada(placa, modelo, data, hora)`.
3. O Controller normaliza a placa, valida o formato, converte data e hora e chama `estacionamento.registrarEntrada(...)`.
4. O Model aplica as regras (placa duplicada, vagas) e grava o `Veiculo` no `ArrayList`.
5. Se algo estiver errado, uma `IllegalArgumentException` sobe até a View, que mostra a mensagem em um `JOptionPane`.

---

## 6. Estrutura do projeto

```
EstacionamentoGrupo08
├── README.md
├── .gitignore
└── src
    ├── Main.java
    ├── model
    │   ├── Perfil.java
    │   ├── Veiculo.java
    │   └── Estacionamento.java
    ├── controller
    │   └── EstacionamentoController.java
    └── view
        ├── Tema.java
        ├── PainelHero.java
        ├── TelaBase.java
        ├── TabelaVeiculos.java
        ├── TelaLogin.java
        ├── TelaPrincipal.java
        ├── TelaEntrada.java
        ├── TelaConsulta.java
        ├── TelaSaida.java
        └── TelaRelatorio.java
```

### Descrição das classes

**Model**

| Classe | Descrição |
|---|---|
| `Perfil` | `enum` com os perfis de acesso (Gerência e Funcionário) e suas senhas. |
| `Veiculo` | Dados de um veículo: placa, modelo, entrada, saída e valor cobrado. Informa se ainda está estacionado. |
| `Estacionamento` | Guarda os veículos em um `ArrayList` e implementa cadastrar, localizar, listar, calcular permanência, calcular valor e registrar saída. Mantém o faturamento acumulado. |

**Controller**

| Classe | Descrição |
|---|---|
| `EstacionamentoController` | Autenticação e perfil logado, validação da placa, conversão de data e hora (`dd/MM/aaaa HH:mm`), montagem dos resumos e das linhas da tabela, formatação de moeda e duração. |

**View**

| Classe | Descrição |
|---|---|
| `TelaLogin` | Tela de acesso (perfil e senha). |
| `TelaPrincipal` | Menu de navegação, resumo da ocupação e atalhos para as demais telas. |
| `TelaEntrada` | Formulário de registro de entrada. |
| `TelaConsulta` | Pesquisa por placa e listagem em `JTable`, com filtro (`JComboBox`). |
| `TelaSaida` | Registro de saída com prévia de permanência e cobrança. |
| `TelaRelatorio` | Indicadores operacionais e faturamento (somente gerência). |
| `TelaBase` | Classe base das telas internas: layout padrão e retorno ao menu. |
| `TabelaVeiculos` | `JTable` estilizada, reutilizada nas telas de consulta e saída. |
| `Tema` | Paleta de cores, fontes e componentes visuais (botão e cartão arredondados, campos). |
| `PainelHero` | Cartão laranja com o carro desenhado em Java2D. |

---

## 7. Componentes Swing utilizados

`JFrame`, `JPanel`, `JLabel`, `JTextField`, `JPasswordField`, `JButton`, `JComboBox`, `JTable`, `JScrollPane`, `JTextArea` e `JOptionPane`, além de `Graphics2D` (Java2D) para o visual personalizado.

---

## 8. Requisitos do trabalho × atendimento

| Requisito | Atendimento |
|---|---|
| Arquitetura MVC | Pacotes `model`, `view` e `controller` |
| Interface exclusivamente em Java Swing | Todas as telas em Swing/Java2D |
| Tela principal com nome do sistema e navegação | `TelaPrincipal` |
| Formulários com `JTextField`, `JComboBox` e `JButton` | `TelaEntrada`, `TelaConsulta`, `TelaSaida` |
| Tabelas `JTable` | `TabelaVeiculos` |
| Mensagens de confirmação e erro com `JOptionPane` | Todas as telas |
| Validações para impedir dados incorretos | `EstacionamentoController` e `Estacionamento` |
| Navegação com retorno à janela principal | Botão "Voltar ao menu" e fechar janela |
| Armazenamento em memória (`ArrayList`) | `Estacionamento.veiculos` |
| Dados disponíveis ao fechar e reabrir janelas | Um único `Estacionamento` vive durante toda a execução |
| Sem banco de dados, web, JavaFX ou frameworks externos | Apenas a biblioteca padrão do Java |
| Sete funcionalidades do tema | Ver seção 2 |

---

## 9. Como executar

### Pré-requisitos

- **JDK 11 ou superior** instalado.
- **Eclipse IDE for Java Developers** (ou outra IDE Java).

### Opção A: clonar do GitHub e abrir no Eclipse

```bash
git clone https://github.com/SEU-USUARIO/NOME-DO-REPOSITORIO.git
```

1. No Eclipse, vá em **File > New > Java Project**, dê o nome `EstacionamentoGrupo08` e desmarque **Create module-info.java**.
2. Copie a pasta `src` do repositório clonado para dentro do projeto (substitua a `src` vazia) e atualize o projeto com **F5**.
3. Clique com o botão direito no projeto, escolha **Properties > Resource** e defina **Text file encoding** como **UTF-8**.
4. Clique com o botão direito em `Main.java` e escolha **Run As > Java Application**.

### Opção B: montar o projeto do zero no Eclipse

1. **File > New > Java Project**, nome `EstacionamentoGrupo08`. Desmarque **Create module-info.java**.
2. Defina o encoding **UTF-8** em **Properties > Resource**.
3. Crie os pacotes `model`, `controller` e `view` dentro de `src`.
4. Crie cada classe no pacote indicado na seção 6 e cole o código correspondente.
5. Salve tudo com **File > Save All** (Ctrl+Shift+S).
6. Clique com o botão direito em `Main.java` e escolha **Run As > Java Application**.

### Opção C: compilar e executar pelo terminal

Na raiz do repositório:

```bash
# Linux / macOS
mkdir -p out
javac -encoding UTF-8 -d out $(find src -name "*.java")
java -cp out Main
```

```bat
:: Windows (cmd)
dir /s /b src\*.java > fontes.txt
javac -encoding UTF-8 -d out @fontes.txt
java -cp out Main
```

### Problemas comuns

| Mensagem ou sintoma | Solução |
|---|---|
| `declared package does not match` | Arraste a classe para o pacote correto no Package Explorer. |
| `X cannot be resolved to a type` | Falta criar ou salvar alguma classe. Crie a que falta e use Ctrl+Shift+O. |
| Acentos quebrados (`Ã§`) | Defina o encoding UTF-8 e cole o código novamente. |
| Erro mencionando `module java.desktop` | Apague o arquivo `module-info.java` do projeto. |

---

## 10. Como usar o sistema

1. **Login:** escolha o perfil, digite a senha e clique em **Entrar** (ou pressione Enter).
2. **Registrar entrada:** menu **Entrada**. Informe placa, modelo, data e horário (ou use "Usar data/hora atual") e clique em **Registrar entrada**.
3. **Consultar veículos:** menu **Consulta**. Veja os estacionados, troque o filtro para "Todos os registros" ou pesquise por placa.
4. **Registrar saída:** menu **Saída**. Clique no veículo na tabela, informe data e horário de saída, use **Calcular cobrança** para ver a prévia e depois **Confirmar saída**.
5. **Relatório:** menu **Relatório** (somente gerência). Mostra ocupação e faturamento; use **Atualizar** para recarregar.
6. **Encerrar sessão:** botão no canto superior direito da tela principal.

**Formatos de entrada:** data `dd/MM/aaaa` (ex.: `25/11/2026`) e hora `HH:mm` (ex.: `14:30`).

---

## 11. Roteiro de testes

| Nº | Ação | Resultado esperado |
|---|---|---|
| 1 | Entrar como **Funcionário** (`func123`) | Menu abre com o **Relatório bloqueado** |
| 2 | Registrar `ABC1D23`, modelo "Gol" | Mensagem de sucesso; vagas disponíveis diminuem em 1 |
| 3 | Registrar `ABC1D23` novamente | Erro de placa já estacionada |
| 4 | Registrar placa `12345` | Erro de placa inválida |
| 5 | Registrar com data `31/02/2026` | Erro de data inválida |
| 6 | Consulta: pesquisar `ABC` | Veículo aparece com situação **Estacionado** |
| 7 | Saída com horário **anterior** à entrada | Erro: saída anterior à entrada |
| 8 | Saída 2h10min após a entrada, **Calcular cobrança** | Permanência `2h 10min` e valor **R$ 15,00** |
| 9 | Confirmar a saída | Veículo sai da lista de estacionados; situação passa a **Saiu** |
| 10 | Encerrar sessão e entrar como **Gerência** (`gerente123`) | Relatório liberado |
| 11 | Abrir o relatório | Faturamento acumulado de **R$ 15,00** |
| 12 | Fechar e reabrir uma tela | Os dados continuam disponíveis |
| 13 | Registrar 21 veículos diferentes | Erro de estacionamento lotado no 21º |

---

## 12. Como personalizar

| O que mudar | Onde |
|---|---|
| Valor da tarifa | `Estacionamento.TARIFA_POR_HORA` |
| Número de vagas | `Estacionamento.CAPACIDADE` |
| Regra de cobrança | método `calcularValor()` em `Estacionamento.java` |
| Senhas dos perfis | `Perfil.java` |
| Cores do sistema | constantes no topo de `Tema.java` |
| Nome da marca | método `logo()` em `Tema.java` |

---

## 13. Limitações conhecidas

- Os dados ficam **somente em memória**: ao fechar a aplicação, tudo é perdido (conforme a especificação do trabalho).
- Os horários são digitados manualmente, com o botão de data/hora atual como atalho.
- A capacidade e a tarifa são constantes no código, sem tela de configuração.
- As senhas são fixas no código.

## 14. Possíveis melhorias futuras

- Persistência em arquivo ou banco de dados.
- Tela de configuração de tarifa e capacidade.
- Tarifas diferentes por tipo de veículo (carro e moto).
- Emissão de comprovante de saída.
- Cadastro de usuários com senha criptografada.

---

## 15. Tecnologias

- Java SE (JDK 11 ou superior)
- Java Swing e Java2D
- Arquitetura MVC
- `ArrayList` para armazenamento em memória
- Eclipse IDE
