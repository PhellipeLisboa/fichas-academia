# Modelo Lógico

Entidades principais:

**Aluno**
- id
- nome
- codigoPublico
- ativo

**Professor**
- id
- nome
- email
- ativo

**Ficha**
- id
- aluno
- professor
- dataInicio
- dataRevisao
- dataReavaliacao
- numeroPlanilha
- intervaloSegundos
- intensidade
- status (RASCUNHO, ATIVA, INATIVA)
- observacoes

**Treino**
- id
- ficha
- nome (A, B, C)
- ordem

**ItemTreino**
- id
- treino
- exercicio
- maquina
- series
- repeticoes
- ordem

**Exercicio**
- id
- nome
- descricao
- videoUrl
- thumbnailUrl
- ativo

**Maquina**
- id
- nome
- numero
- ativo

**Presenca (atualização futura)**
- id
- aluno
- data
- professor
- observacao

Relacionamentos:

Aluno 1..N Ficha  
Ficha 1..N Treino  
Treino 1..N ItemTreino  
ItemTreino N..1 Exercicio  
ItemTreino N..1 Maquina  