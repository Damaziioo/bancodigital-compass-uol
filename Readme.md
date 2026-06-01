# Banco Digital API

## Sobre o projeto

_Resolução de Desafio técnico da Compass UOL._ 

A API REST foi desenvolvida para simular operações de um banco digital,
contemplando gerenciamento de contas e transferências entre clientes. 
O projeto foi construído com foco em consistência transacional,
controle de concorrência e boas práticas de arquitetura.

---

## Tecnologias

| Tecnologia | Versão | Finalidade |
|-----------|--------|-----------|
| Java | 21 | Linguagem principal |
| Spring Boot | 3.5.14| Framework base |
| Spring Data JPA | - | Persistência de dados |
| H2 Database | - | Banco in-memory |
| SpringDoc OpenAPI | 2.8.17 | Documentação Swagger |
| JUnit 5 + Mockito | - | Testes unitários |
| Maven | - | Gerenciador de dependências |

---

## Como rodar

### Pré-requisitos
- Java 21
- Maven

### Passos

```bash
# Clone o repositório
git clone https://github.com/Damaziioo/bancodigital-compass-uol

# Acesse a pasta
cd bancodigital

# Execute o projeto
./mvnw spring-boot:run
```

### Acesse

| Recurso | URL |
|---------|-----|
| Swagger UI | http://localhost:8080/swagger-ui.html |
| H2 Console | http://localhost:8080/h2-console |

### Configuração do H2 Console

```
JDBC URL: jdbc:h2:mem:bancodigital
Usuário:  sa
Senha:    (deixe vazio)
```

---

## Contas pré-carregadas

O sistema já sobe com 3 contas para facilitar os testes:

| Nome | Saldo |
|------|-------|
| João Silva | R$ 1.000,00 |
| Maria Souza | R$ 2.500,00 |
| Carlos Lima | R$ 500,00 |

> Os IDs são gerados automaticamente via UUID. Utilize o endpoint `GET /v1/contas` para consultá-los.

---

## Endpoints

### Contas

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `POST` | `/v1/contas` | Criar uma nova conta |
| `GET` | `/v1/contas` | Listar todas as contas |
| `GET` | `/v1/contas/{id}` | Buscar conta por ID |

### Transferências

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `POST` | `/v1/transferencias` | Realizar uma transferência |
| `GET` | `/v1/transferencias/{contaId}` | Histórico de transferências da conta |

---

## Decisões de arquitetura

### Arquitetura em camadas
O projeto segue o padrão de separação em camadas (Controller, Service e Repository) garantindo baixo acoplamento e alta coesão. Cada camada tem uma responsabilidade clara, facilitando manutenção, testabilidade e evolução do código.

### H2 in-memory
Escolhido para eliminar dependências externas e garantir que qualquer pessoa consiga rodar o projeto com um único comando. Em produção, seria substituído por um banco relacional robusto como PostgreSQL.

### UUID como identificador
A escolha de UUID no lugar de Long sequencial evita a exposição de informações sobre o volume de dados do sistema e dificulta ataques de enumeração de recursos. Tal prática é recomendada em APIs financeiras.

### Pessimistic Lock nas transferências
Em cenários de alta concorrência, duas requisições simultâneas de transferência da mesma conta poderiam passar na validação de saldo ao mesmo tempo, resultando em saldo negativo. O `PESSIMISTIC_WRITE` bloqueia o registro no banco durante a transação, garantindo que apenas uma operação por vez altere o saldo de cada conta.

### @Transactional com rollback automático
Todo o fluxo de transferência, debitar, creditar e registrar, acontece dentro de uma única transação. Se qualquer etapa falhar, o rollback é aplicado automaticamente, garantindo que nunca haja débito sem crédito correspondente.

### Notificações assíncronas com @Async
O envio de notificações ocorre em uma thread separada após a conclusão da transferência. Isso garante que uma eventual falha na notificação não afete o fluxo principal e que a resposta da API seja entregue sem aguardar o processamento da notificação.

---

## Evoluções futuras

### Redis como camada de cache e controle de idempotência
Antes de qualquer operação de transferência bater no banco, uma camada de Redis poderia verificar se aquela requisição já foi processada, evitando transferências duplicadas em casos de retry. Além disso, consultas frequentes como saldo e histórico poderiam ser cacheadas, reduzindo drasticamente a carga no banco de dados.

### Autenticação e autorização com Spring Security + JWT
Hoje a API é aberta. Em produção, cada cliente deveria se autenticar via JWT e só ter acesso às suas próprias contas. O Spring Security permitiria implementar isso de forma robusta com controle fino de permissões por endpoint, impedindo, por exemplo, que um cliente consulte o saldo de outro.

### Notificações via Kafka ou RabbitMQ
A implementação atual simula o envio de notificações com `@Async`. Em um ambiente produtivo de alta escala, esse fluxo seria publicado em um tópico Kafka ou fila RabbitMQ, permitindo múltiplos consumidores, reprocessamento em caso de falha e rastreabilidade completa de cada notificação enviada.

### Arquitetura de microsserviços
O projeto foi estruturado pensando em uma futura decomposição em microsserviços independentes:
- **ms-contas**: responsável pelo ciclo de vida das contas e consulta de saldo
- **ms-transferencias**: responsável pelo processamento das transferências e registro de movimentações
- **ms-notificacoes**: consumidor de eventos, responsável pelo envio de notificações aos clientes

Cada serviço teria seu próprio banco de dados, se comunicaria via eventos e poderia ser escalado de forma independente conforme a demanda.

### Observabilidade
Integração com Spring Actuator + Micrometer + Prometheus e Grafana para monitoramento de métricas em tempo real, como a latência das transferências, a taxa de erros e o volume de operações por segundo, o que é essencial em um sistema financeiro.

---

## Autor

_Desenvolvido por **Lucas Damazio**_