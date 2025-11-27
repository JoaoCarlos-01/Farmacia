package Farmacia;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class ControleEstoqueFarmacia {
    private static final DateTimeFormatter DATEFMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final Scanner SCANNER = new Scanner(System.in);
    private static final Estoque ESTOQUE = new Estoque();
    private static final Secundaria SECUNDARIA = new Secundaria(SCANNER, ESTOQUE, DATEFMT);

    public static void main(String[] args) {
        menuPrincipal();
    }

    private static void menuPrincipal() {
        while (true) {
            System.out.print("-------------------------------------");
            System.out.println("\n Sistema de Controle de Estoque ");
            System.out.println("1) Cadastrar Produto");
            System.out.println("2) Registrar Entrada");
            System.out.println("3) Registrar Saída");
            System.out.println("4) Consultar Produto");
            System.out.println("5) Relatórios");
            System.out.println("6) Sair");
            System.out.println("-------------------------------------");
            System.out.println("Escolha uma opção: ");

            String opcao = SCANNER.nextLine();
            switch (opcao) {
                case "1":
                    SECUNDARIA.cadastrarProduto();
                    break;
                case "2":
                    SECUNDARIA.registrarEntrada();
                    break;
                case "3":
                    SECUNDARIA.registrarSaida();
                    break;
                case "4":
                    SECUNDARIA.consultarProduto();
                    break;
                case "5":
                    SECUNDARIA.gerarRelatorios();
                    break;
                case "6":
                    System.out.println("Saindo...");
                    return;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }
}