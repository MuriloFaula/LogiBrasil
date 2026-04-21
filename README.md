# LogiBrasil

Sistema de gerenciamento de armazém logístico desenvolvido em Java. O projeto implementa estruturas de dados do zero, sem uso de bibliotecas prontas, aplicando os conceitos de pilha (LIFO), matriz bidimensional e algoritmo de reempilhamento no contexto de um armazém de distribuição.

---

## Tecnologias

- Java 21
- NetBeans / Maven

---

## Estrutura

```
src/
├── model/
│   ├── Caixa.java         # Entidade principal do sistema
│   ├── Pilha.java         # Implementação manual de pilha com vetor
│   └── Setor.java         # Setor do armazém com pilha interna
├── armazem/
│   └── Armazem.java       # Matriz de setores e operações CRUD
├── relatorio/
│   └── Dashboard.java     # Relatório de ocupação por setor
└── LogiBrasil.java        # Ponto de entrada e menu interativo
```

---

## Funcionalidades

- Adição e remoção de caixas por setor
- Remoção de caixa específica com reempilhamento
- Busca por ID percorrendo a matriz de setores
- Relatório de ocupação com alerta acima de 80%
- Validação de ID duplicado

---

## Como rodar

```bash
git clone https://github.com/MuriloFaula/LogiBrasil.git
```

Abra no NetBeans e execute `LogiBrasil.java`.

---

Murilo Faula — Ciência da Computação, 7º semestre
