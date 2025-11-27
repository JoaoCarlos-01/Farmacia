package Farmacia;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Secundaria {
    private final Scanner scanner;
    private final Estoque estoque;
    private final DateTimeFormatter dateFmt;

    public Secundaria(Scanner scanner, Estoque estoque, DateTimeFormatter dateFmt) {
        this.scanner = scanner;
        this.estoque = estoque;
        this.dateFmt = dateFmt;
    }

    public void cadastrarProduto() {
        try {
            System.out.println("\n Cadastro de Produto ");
            System.out.print("Id: ");
            String id = scanner.nextLine();
            if (estoque.existeProduto(id)) {
                System.out.println("Id já cadastrado!");
                return;
            }
            System.out.print("Nome do produto: ");
            String nome = scanner.nextLine();
            System.out.print("Categoria do produto: ");
            String categoria = scanner.nextLine();
            System.out.print("Quantidade inicial: ");
            int quantidade = Integer.parseInt(scanner.nextLine());
            if (quantidade < 0) {
                System.out.println("Quantidade não pode ser negativa!");
                return;
            }
            System.out.print("Preço unitário: ");
            double preco = Double.parseDouble(scanner.nextLine());
            if (preco < 0) {
                System.out.println("Preço não pode ser negativo!");
                return;
            }
            System.out.print("Data de validade (dd/MM/yyyy): ");
            LocalDate validade = LocalDate.parse(scanner.nextLine(), dateFmt);

            Produto p = new Produto(id, nome, categoria, quantidade, preco, validade);
            estoque.adicionarProduto(p);
            System.out.println("Produto cadastrado com sucesso!");
        } catch (NumberFormatException e) {
            System.out.println("Erro: Valor numérico inválido!");
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    public void registrarEntrada() {
        try {
            System.out.print("\nId do produto: ");
            String id = scanner.nextLine();
            Produto p = estoque.buscarProduto(id);
            if (p == null) {
                System.out.println("Produto não encontrado!");
                return;
            }
            System.out.print("Quantidade de entrada: ");
            int qtd = Integer.parseInt(scanner.nextLine());
            if (qtd <= 0) {
                System.out.println("Quantidade deve ser maior que zero!");
                return;
            }
            p.adicionarEstoque(qtd);
            estoque.registrarMovimentacao(new Movimentacao(id, "Entrada", qtd, LocalDate.now()));
            System.out.println("Entrada registrada!");
        } catch (NumberFormatException e) {
            System.out.println("Erro: Quantidade inválida!");
        }
    }

    public void registrarSaida() {
        try {
            System.out.print("\nId do produto: ");
            String id = scanner.nextLine();
            Produto p = estoque.buscarProduto(id);
            if (p == null) {
                System.out.println("Produto não encontrado!");
                return;
            }
            System.out.print("Quantidade de saída: ");
            int qtd = Integer.parseInt(scanner.nextLine());
            if (qtd <= 0) {
                System.out.println("Quantidade deve ser maior que zero!");
                return;
            }
            if (p.getQuantidade() < qtd) {
                System.out.println("Estoque insuficiente!");
                return;
            }
            p.removerEstoque(qtd);
            estoque.registrarMovimentacao(new Movimentacao(id, "Saída", qtd, LocalDate.now()));
            System.out.println("Saída registrada!");
        } catch (NumberFormatException e) {
            System.out.println("Erro: Quantidade inválida!");
        }
    }

    public void consultarProduto() {
        System.out.print("\nBuscar por nome ou Id: ");
        String termo = scanner.nextLine().toLowerCase();
        for (Produto p : estoque.getProdutos()) {
            if (p.getId().equalsIgnoreCase(termo) || p.getNome().toLowerCase().contains(termo)) {
                System.out.println(p);
            }
        }
    }

    public void gerarRelatorios() {
        boolean exibindo = true;
        while (exibindo) {
            System.out.println("\n1) Movimentações");
            System.out.println("2) Produtos disponíveis");
            System.out.println("3) Produtos vencidos");
            System.out.println("4) Itens a vencer (em até 30 dias)");
            System.out.println("5) Voltar para o menu principal");
            System.out.print("Escolha: ");
            String opcao = scanner.nextLine();

            switch (opcao) {
                case "1":
                    estoque.exibirMovimentacoes();
                    break;
                case "2":
                    estoque.exibirProdutos();
                    break;
                case "3":
                    estoque.exibirVencidos();
                    break;
                case "4":
                    estoque.exibirAVencer(30);
                    break;
                case "5":
                    exibindo = false;
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }
}

class Produto {
    private String id;
    private String nome;
    private String categoria;
    private int quantidade;
    private double preco;
    private LocalDate validade;

    public Produto(String id, String nome, String categoria, int quantidade, double preco, LocalDate validade) {
        this.id = id;
        this.nome = nome;
        this.categoria = categoria;
        this.quantidade = quantidade;
        this.preco = preco;
        this.validade = validade;
    }

    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public LocalDate getValidade() {
        return validade;
    }

    public void adicionarEstoque(int qtd) {
        quantidade += qtd;
    }

    public void removerEstoque(int qtd) {
        quantidade -= qtd;
    }

    public boolean vencido() {
        return validade.isBefore(LocalDate.now());
    }

    public boolean aVencer(int dias) {
        if (vencido()) {
            return false;
        }
        long diasRestantes = ChronoUnit.DAYS.between(LocalDate.now(), validade);
        return diasRestantes >= 0 && diasRestantes <= dias;
    }

    @Override
    public String toString() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return String.format("Id: %s | Nome: %s | Categoria: %s | Quantidade: %d | Preço: %.2f | Validade: %s",
                id, nome, categoria, quantidade, preco, validade.format(fmt));
    }
}

class Movimentacao {
    private String idProduto;
    private String tipo;
    private int quantidade;
    private LocalDate data;

    public Movimentacao(String idProduto, String tipo, int quantidade, LocalDate data) {
        this.idProduto = idProduto;
        this.tipo = tipo;
        this.quantidade = quantidade;
        this.data = data;
    }

    @Override
    public String toString() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return String.format("%s - %s - %d unidades em %s", tipo, idProduto, quantidade, data.format(fmt));
    }
}

class Estoque {
    private List<Produto> produtos = new ArrayList<>();
    private List<Movimentacao> movimentacoes = new ArrayList<>();

    public void adicionarProduto(Produto p) {
        produtos.add(p);
    }

    public boolean existeProduto(String id) {
        return produtos.stream().anyMatch(p -> p.getId().equalsIgnoreCase(id));
    }

    public Produto buscarProduto(String id) {
        for (Produto p : produtos)
            if (p.getId().equalsIgnoreCase(id))
                return p;
        return null;
    }

    public List<Produto> getProdutos() {
        return produtos;
    }

    public void registrarMovimentacao(Movimentacao m) {
        movimentacoes.add(m);
    }

    public void exibirMovimentacoes() {
        System.out.println("\n Movimentações ");
        for (Movimentacao m : movimentacoes)
            System.out.println(m);
    }

    public void exibirProdutos() {
        System.out.println("\n Produtos ");
        for (Produto p : produtos)
            System.out.println(p);
    }

    public void exibirVencidos() {
        System.out.println("\n Produtos Vencidos ");
        for (Produto p : produtos)
            if (p.vencido())
                System.out.println(p);
    }

    public void exibirAVencer(int dias) {
        System.out.println("\n Produtos a vencer em até " + dias + " dias ");
        for (Produto p : produtos)
            if (!p.vencido() && p.aVencer(dias))
                System.out.println(p);
    }
}