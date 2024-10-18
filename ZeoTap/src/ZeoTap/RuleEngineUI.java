package ZeoTap;  // Ensure the package name matches the folder structure

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import javax.swing.*;

class Node {
    String type;
    Node left, right;
    String value;

    public Node(String type, Node left, Node right, String value) {
        this.type = type;
        this.left = left;
        this.right = right;
        this.value = value;
    }
}

public class RuleEngineUI extends JFrame {
    private JTextField ruleInput;
    private JTextArea displayArea;
    private List<Node> ruleNodes;

    public RuleEngineUI() {
        ruleNodes = new ArrayList<>();

        // Frame settings
        setTitle("Rule Engine");
        setSize(600, 400);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Input area for rule
        ruleInput = new JTextField();
        add(ruleInput, BorderLayout.NORTH);

        // Buttons
        JPanel buttonPanel = new JPanel();
        JButton createRuleBtn = new JButton("Create Rule");
        JButton combineRulesBtn = new JButton("Combine Rules");
        JButton evaluateRuleBtn = new JButton("Evaluate Rule");

        buttonPanel.add(createRuleBtn);
        buttonPanel.add(combineRulesBtn);
        buttonPanel.add(evaluateRuleBtn);
        add(buttonPanel, BorderLayout.CENTER);

        // Display area for results
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        add(new JScrollPane(displayArea), BorderLayout.SOUTH);

        // Button Action Listeners
        createRuleBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String rule = ruleInput.getText().trim();
                if (rule.isEmpty()) {
                    displayArea.append("Error: Rule input is empty!\n");
                    return;
                }

                try {
                    Node node = createRule(rule);
                    ruleNodes.add(node);
                    displayArea.append("Rule Created: " + rule + "\n");
                } catch (Exception ex) {
                    displayArea.append("Error creating rule: " + ex.getMessage() + "\n");
                }
            }
        });

        combineRulesBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (ruleNodes.size() > 1) {
                    Node combinedRule = combineRules(ruleNodes);
                    ruleNodes.clear(); // Reset after combining
                    ruleNodes.add(combinedRule);
                    displayArea.append("Rules Combined.\n");
                } else {
                    displayArea.append("Not enough rules to combine.\n");
                }
            }
        });

        evaluateRuleBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!ruleNodes.isEmpty()) {
                    Map<String, Object> data = new HashMap<>();
                    data.put("age", 35); // Simulated data
                    data.put("department", "sales");
                    data.put("salary", 60000);
                    data.put("experience", 3);

                    try {
                        boolean result = evaluateRule(ruleNodes.get(0), data);
                        displayArea.append("Evaluation Result: " + result + "\n");
                    } catch (Exception ex) {
                        displayArea.append("Error evaluating rule: " + ex.getMessage() + "\n");
                    }
                } else {
                    displayArea.append("No rule to evaluate.\n");
                }
            }
        });
    }

    // AST construction
    public Node createRule(String rule) {
        if (rule.contains("AND")) {
            String[] parts = rule.split("AND");
            Node left = createRule(parts[0].trim());
            Node right = createRule(parts[1].trim());
            return new Node("operator", left, right, "AND");
        } else if (rule.contains("OR")) {
            String[] parts = rule.split("OR");
            Node left = createRule(parts[0].trim());
            Node right = createRule(parts[1].trim());
            return new Node("operator", left, right, "OR");
        } else {
            return new Node("operand", null, null, rule.trim());
        }
    }

    // Rule combination
    public Node combineRules(List<Node> rules) {
        Node combined = rules.get(0);
        for (int i = 1; i < rules.size(); i++) {
            combined = new Node("operator", combined, rules.get(i), "AND");
        }
        return combined;
    }

//    // Rule evaluation with debug prints

    public boolean evaluateRule(Node root, Map<String, Object> data) throws Exception {
        if (root.type.equals("operand")) {
            String condition = root.value;
            String[] parts = condition.split(" ");
            if (parts.length != 3) {
                throw new Exception("Invalid condition: " + condition);
            }

            String key = parts[0];
            String operator = parts[1];
            String value = parts[2].replace("'", ""); // Remove quotes around string values

            if (!data.containsKey(key)) {
                throw new Exception("Key not found in data: " + key);
            }

            Object dataValue = data.get(key);

            // Check if the key corresponds to a string and handle case-insensitive comparison
            if (dataValue instanceof String) {
                String dataStr = ((String) dataValue).toLowerCase();  // Convert both to lowercase for case-insensitive comparison
                String valueStr = value.toLowerCase();

                if (operator.equals("=")) {
                    return dataStr.equals(valueStr);
                } else {
                    throw new Exception("Unsupported operator for string comparison: " + operator);
                }
            } else if (dataValue instanceof Integer) {
                int dataInt = (int) data.get(key);
                int valueInt = Integer.parseInt(value);
                int intValue = Integer.parseInt(value);
             int dataIntValue = (int) dataValue;

                switch (operator) {
                    case ">":
                    	boolean result = dataIntValue > intValue;
                        
                        System.out.println("Evaluating: " + condition + " => " + result);
                        return result;
                    case "<":
                    	 result = dataIntValue < intValue;
                    	
                    	System.out.println("Evaluating: " + condition + " => " + result);
                        return result;
                        
                    case "=":
                    	result = dataIntValue == intValue;
                    System.out.println("Evaluating: " + condition + " => " + result);
                    return result;
                    default:
                        throw new Exception("Unsupported operator: " + operator);
                }
            }
        } else if (root.type.equals("operator")) {
            boolean leftResult = evaluateRule(root.left, data);
            boolean rightResult = evaluateRule(root.right, data);
            System.out.println("Evaluating: " + root.value + " between left: " + leftResult + " and right: " + rightResult);
            if (root.value.equals("AND")) {
                return leftResult && rightResult;
                
            } else if (root.value.equals("OR")) {
                return leftResult || rightResult;
            }
        }
        throw new Exception("Invalid node type");
    }



    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            RuleEngineUI ruleEngineUI = new RuleEngineUI();
            ruleEngineUI.setVisible(true);
        });
    }
}
