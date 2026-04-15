# 🛡️ ShopFast - Software Quality Assurance Plan (Mini-SQAP)

## 📌 Contexto
Durante a Black Friday, a plataforma ShopFast sofreu uma falha crítica: cupons de desconto foram aplicados sem validação de saldo, resultando em expedição de pedidos não pagos. Isso comprometeu diretamente a **Adequação Funcional (ISO 25010)** e elevou o **Change Failure Rate (DORA)**.

Este repositório estabelece uma abordagem preventiva baseada em **SQA (IEEE 730)**, substituindo validações reativas por governança automatizada de qualidade.

---

# 🚨 A. POLÍTICA DE QUALITY GATE (IEEE 730)

## 🔒 Regra 1 — Validação Transacional Obrigatória (Enforcement Automatizado)

**Nenhum código será promovido para produção se não existir validação explícita de integridade financeira (saldo ≥ valor final) antes da confirmação do pedido.**

### Enforcement:
- Pipeline CI bloqueia merge se:
    - Não houver teste automatizado cobrindo cenário de:
        - cupom válido + saldo insuficiente
- Coverage mínima obrigatória: **90% na camada de domínio**
- Ferramentas:
    - Jacoco (coverage)
    - SonarQube (quality gate)

---

## 🔒 Regra 2 — Isolamento de Domínio Crítico (Clean Architecture Enforcement)

**Regras de negócio críticas (cálculo de desconto + validação de pagamento) devem estar isoladas na camada de domínio, proibidas de depender de API externa, UI ou infraestrutura.**

### Enforcement:
- Análise estática (SonarQube) bloqueia:
    - dependências entre `domain` → `controller` ou `external`
- Code review obrigatório com checklist SQA
- Build falha se houver violação de arquitetura

---

# ⚠️ B. SUMÁRIO EXECUTIVO — MATRIZ DE RISCO (PxI)

## 📊 Classificação do Risco

| Fator          | Avaliação |
|----------------|----------|
| Probabilidade  | Alta     |
| Impacto        | Crítico  |
| Classificação  | 🔴 Risco Extremo |

### 📌 Descrição
A ausência de validação transacional entre aplicação de cupom e verificação de saldo permitiu que pedidos fossem aprovados sem pagamento confirmado. Esse tipo de falha possui alta probabilidade em ambientes com pressão de entrega e impacto financeiro direto, afetando receita e logística.

### 🎯 Mitigação via SQA
A política implementada atua diretamente na origem do problema (processo), impedindo que código inválido seja integrado ao sistema. Ao exigir validação automatizada, cobertura mínima e isolamento de domínio, eliminamos dependência de validação manual e reduzimos drasticamente o **Change Failure Rate**, garantindo previsibilidade e integridade operacional.

---

# 🧩 Código de Domínio (Clean Code + Regra de Negócio)

## 📌 Snippet — Regra Crítica de Validação

```java
public double processPurchase(String coupon, double originalAmount, double userBalance) {
    double discountedAmount = applyDiscount(coupon, originalAmount);
    validateTransaction(discountedAmount, userBalance);
    return discountedAmount;
}

private void validateTransaction(double finalAmount, double userBalance) {
    if (userBalance < finalAmount) {
        throw new IllegalStateException("Saldo insuficiente para concluir a compra.");
    }
}