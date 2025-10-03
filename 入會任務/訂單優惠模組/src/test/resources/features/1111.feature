@1111_promotion @stacking
Feature: E-commerce Order Pricing - 11/11 Quantity Discount and Stacking

  As a professional QC engineer
  I want to verify the 11/11 quantity discount logic
  So that it works correctly alone and stacks accurately with other promotions.

  Background:
    # 共同配置：配置既有的兩種促銷，以便測試混合情境。
    Given the threshold discount promotion is configured:
      | threshold | discount |
      | 1000      | 100      |
    And the buy one get one promotion for cosmetics is active
    And the 11/11 quantity discount promotion is active
    # 促销规则：同一商品每满 10 件，其中 10 件享 20% 折扣。

# --- Set 1: 11/11 优惠單獨作用 (基礎與邊界測試) ---

  Scenario: S1.1: 11/11 Discount - Example 1 (12 items)
    # 驗證單獨作用與計算邏輯 (1200 元 - 200 元折扣 = 1000 元)
    When a customer places an order with:
      | productName | category  | quantity | unitPrice |
      | 襪子          | apparel   | 12       | 100       |
    Then the order summary should be:
      | originalAmount | discount | totalAmount |
      | 1200           | 200      | 1000        |
    And the customer should receive:
      | productName | quantity |
      | 襪子          | 12       |

  Scenario: S1.2: 11/11 Discount - Example 2 (27 items)
    # 驗證多組折扣計算邏輯 (2700 元 - 400 元折扣 = 2300 元)
    When a customer places an order with:
      | productName | category  | quantity | unitPrice |
      | 襪子          | apparel   | 27       | 100       |
    Then the order summary should be:
      | originalAmount | discount | totalAmount |
      | 2700           | 400      | 2300        |
    And the customer should receive:
      | productName | quantity |
      | 襪子          | 27       |

  Scenario: S1.3: 11/11 Discount - Example 3 (10 mixed items, No Discount)
    # 驗證“同一種商品”的互斥條件
    When a customer places an order with:
      | productName | category  | quantity | unitPrice |
      | A           | misc      | 1        | 100       |
      | B           | misc      | 1        | 100       |
      | C           | misc      | 1        | 100       |
      | D           | misc      | 7        | 100       | # 故意讓其中一項接近門檻
    Then the order summary should be:
      | originalAmount | discount | totalAmount |
      | 1000           | 0        | 1000        |
    And the customer should receive:
      | productName | quantity |
      | A           | 1        |
      | B           | 1        |
      | C           | 1        |
      | D           | 7        |
      
  Scenario: S1.4: 11/11 Discount - Boundary Condition (10 items exactly)
    # 驗證最小觸發門檻 (10 件 * 500 元 = 5000 元, 11/11 折扣 1000 元)
    # 由於 Background 已設定滿額折扣，此情境自動變為 S2.1 的變形。
    # 原始金額: 5000。 11/11 折扣 1000。 子總價 4000 (>1000)。 滿額折扣 100。 總折扣 1100。 總價 3900
    When a customer places an order with:
      | productName | category  | quantity | unitPrice |
      | T-shirt     | apparel   | 10       | 500       |
    Then the order summary should be:
      | originalAmount | discount | totalAmount |
      | 5000           | 1100     | 3900        |
    And the customer should receive:
      | productName | quantity |
      | T-shirt     | 10       |

# --- Set 2: 11/11 优惠 + 滿額折扣 (Threshold Discount) ---

  Scenario: S2.1: 11/11 Discount + Threshold (Discount is based on Subtotal)
    # 驗證 11/11 折扣計算完成後，再觸發滿額折扣的堆疊順序。
    # T-shirt: 12*500=6000。 11/11 折扣 1000。 子總價 5000 (>1000)。 滿額折扣 100。 總折扣 1100。 總價 4900
    When a customer places an order with:
      | productName | category  | quantity | unitPrice |
      | T-shirt     | apparel   | 12       | 500       |
    Then the order summary should be:
      | originalAmount | discount | totalAmount |
      | 6000           | 1100     | 4900        |
    And the customer should receive:
      | productName | quantity |
      | T-shirt     | 12       |

  Scenario: S2.2: 11/11 Discount - Prevents Threshold from Activating (Subtotal < 1000)
    # 驗證 11/11 折扣可能導致滿額折扣無法觸發的邊界情況。
    # 原始總價: 10*100=1000。 11/11 折扣 200。 子總價 800 (<1000)。 滿額折扣 0。 總折扣 200。 總價 800
    When a customer places an order with:
      | productName | category  | quantity | unitPrice |
      | 襪子          | apparel   | 10       | 100       |
    Then the order summary should be:
      | originalAmount | discount | totalAmount |
      | 1000           | 200      | 800         |
    And the customer should receive:
      | productName | quantity |
      | 襪子          | 10       |

# --- Set 3: 11/11 优惠 + 買一送一 (BOGO for Cosmetics) ---

  Scenario: S3.1: 11/11 Discount + BOGO (Different Item Categories)
    # 驗證兩種 Item Level 的促銷同時發生，且互不影響收到的數量 (只有 BOGO 影響數量)。
    # T-shirt: 12*500=6000, 11/11 折扣 1000
    # 口紅: 2*300=600, BOGO 觸發 (收到 3 件)
    # 原始總價: 6600。 11/11 折扣 1000。 子總價 5600 (>1000)。 滿額折扣 100。 總折扣 1100。 總價 5500
    When a customer places an order with:
      | productName | category  | quantity | unitPrice |
      | T-shirt     | apparel   | 12       | 500       |
      | 口紅          | cosmetics | 2        | 300       |
    Then the order summary should be:
      | originalAmount | discount | totalAmount |
      | 6600           | 1100     | 5500        |
    And the customer should receive:
      | productName | quantity |
      | T-shirt     | 12       |
      | 口紅          | 3        |

  Scenario: S3.2: 11/11 Discount - Does not apply to BOGO items
    # 驗證 BOGO 商品數量超過 10 件時，是否誤觸 11/11 優惠 (假設 BOGO 優先且互斥)。
    # 假設 BOGO 優先，且 BOGO 類別不參與 11/11 促銷。
    # 口紅: 11*300=3300。 BOGO 觸發 (收到 12 件)。 11/11 折扣 0 (假設互斥)。
    # 原始總價: 3300。 滿額折扣 100 (3300>1000)。 總折扣 100。 總價 3200。
    When a customer places an order with:
      | productName | category  | quantity | unitPrice |
      | 口紅          | cosmetics | 11       | 300       |
    Then the order summary should be:
      | originalAmount | discount | totalAmount |
      | 3300           | 100      | 3200        |
    And the customer should receive:
      | productName | quantity |
      | 口紅          | 12       |
