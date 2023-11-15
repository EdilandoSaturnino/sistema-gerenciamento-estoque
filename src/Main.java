import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import br.senai.sp.cotia.models.Categoria;
import br.senai.sp.cotia.models.Cliente;
import br.senai.sp.cotia.models.Pedido;
import br.senai.sp.cotia.models.Produto;
import br.senai.sp.cotia.models.Vendedor;
import br.senai.sp.cotia.utils.CSVExporter;

public class Main {
    private static List<Vendedor> vendedores = new ArrayList<>();
    private static List<Cliente> clientes = new ArrayList<>();
    private static List<Produto> produtos = new ArrayList<>();
    private static List<Pedido> pedidos = new ArrayList<>();

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int opcao;

        do {
            System.out.println("----- Menu -----");
            System.out.println("1. Cadastrar Vendedor");
            System.out.println("2. Cadastrar Cliente");
            System.out.println("3. Cadastrar Produto");
            System.out.println("4. Realizar Venda");
            System.out.println("5. Exportar Produtos para CSV");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");
            opcao = sc.nextInt();
            sc.nextLine();

            switch (opcao) {
                case 1:
                    cadastrarVendedor(sc);
                    break;
                case 2:
                    cadastrarCliente(sc);
                    break;
                case 3:
                    cadastrarProduto(sc);
                    break;
                case 4:
                    realizarVenda(sc);
                    break;
                case 5:
                    exportarProdutosParaCSV(sc);
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida.");
            }

        } while (opcao != 6);
    }

    private static void cadastrarVendedor(Scanner sc) {
        System.out.print("Nome Completo: ");
        String nome = sc.nextLine();
        System.out.print("Email: ");
        String email = sc.nextLine();
        System.out.print("CPF: ");
        String cpf = sc.nextLine();
        System.out.print("RG: ");
        String rg = sc.nextLine();
        System.out.print("Endereço: ");
        String endereco = sc.nextLine();

        Vendedor vendedor = new Vendedor();
        vendedor.setNomeCompleto(nome);
        vendedor.setEmail(email);
        vendedor.setCpf(cpf);
        vendedor.setRg(rg);
        vendedor.setEndereco(endereco);
        vendedores.add(vendedor);

        System.out.println("Vendedor cadastrado com sucesso.");
    }

    private static void cadastrarCliente(Scanner sc) {
        System.out.print("CPF/CNPJ: ");
        String cpfCnpj = sc.nextLine();
        System.out.print("Telefone: ");
        String telefone = sc.nextLine();
        System.out.print("Nome: ");
        String nome = sc.nextLine();
        System.out.print("Email: ");
        String email = sc.nextLine();
        System.out.print("Endereço: ");
        String endereco = sc.nextLine();

        Cliente cliente = new Cliente();
        cliente.setCpfCnpj(cpfCnpj);
        cliente.setTelefone(telefone);
        cliente.setNome(nome);
        cliente.setEmail(email);
        cliente.setEndereco(endereco);
        clientes.add(cliente);

        System.out.println("Cliente cadastrado com sucesso.");
    }

    private static void cadastrarProduto(Scanner sc) {
        System.out.print("Nome: ");
        String nome = sc.nextLine();
        System.out.print("Categoria (MASCULINO, FEMININO, INFANTIL): ");
        Categoria categoria = Categoria.valueOf(sc.nextLine().toUpperCase());
        System.out.print("Preço: ");
        double preco = sc.nextDouble();
        sc.nextLine();

        Produto produto = new Produto();
        produto.setNome(nome);
        produto.setCategoria(categoria);
        produto.setPreco(preco);
        produtos.add(produto);

        System.out.println("Produto cadastrado com sucesso.");
    }

    private static void realizarVenda(Scanner sc) {
        System.out.println("----- Vendedores -----");
        for (int i = 0; i < vendedores.size(); i++) {
            System.out.println("ID: " + (i + 1) + ", Nome: " + vendedores.get(i).getNomeCompleto());
        }

        System.out.print("Escolha o ID do Vendedor: ");
        int idVendedor = sc.nextInt();
        sc.nextLine();

        System.out.println("----- Clientes -----");
        for (int i = 0; i < clientes.size(); i++) {
            System.out.println("ID: " + (i + 1) + ", Nome: " + clientes.get(i).getNome());
        }

        System.out.print("Escolha o ID do Cliente: ");
        int idCliente = sc.nextInt();
        sc.nextLine();

        if (idVendedor > 0 && idVendedor <= vendedores.size() &&
                idCliente > 0 && idCliente <= clientes.size()) {

            Vendedor vendedor = vendedores.get(idVendedor - 1);
            Cliente cliente = clientes.get(idCliente - 1);

            Map<Produto, Integer> produtosVenda = new HashMap<>();

            String maisProdutos;
            do {
                System.out.println("----- Produtos -----");
                for (int i = 0; i < produtos.size(); i++) {
                    System.out.println("ID: " + (i + 1) + ", Nome: " + produtos.get(i).getNome());
                }

                System.out.print("Escolha o ID do Produto: ");
                int idProduto = sc.nextInt();
                sc.nextLine();

                if (idProduto > 0 && idProduto <= produtos.size()) {
                    Produto produto = produtos.get(idProduto - 1);

                    System.out.print("Quantidade: ");
                    int quantidade = sc.nextInt();
                    sc.nextLine();

                    produtosVenda.put(produto, quantidade);
                } else {
                    System.out.println("ID de produto inválido.");
                }

                System.out.print("Deseja adicionar mais produtos? (s/n): ");
                maisProdutos = sc.nextLine();
            } while ("s".equalsIgnoreCase(maisProdutos));

            Pedido pedido = new Pedido();
            pedido.setVendedor(vendedor);
            pedido.setCliente(cliente);
            pedido.setProdutos(produtosVenda);
            pedidos.add(pedido);

            System.out.println("Venda realizada com sucesso.");
        }
    }

    private static void exportarProdutosParaCSV(Scanner sc) {
        if (produtos.isEmpty()) {
            System.out.println("Nenhum produto disponível para exportar.");
            return;
        }

        System.out.print("Digite o nome do arquivo (sem extensão): ");
        String fileName = sc.nextLine() + ".csv";

        CSVExporter.exportData(produtos, vendedores, clientes, pedidos, fileName);
        System.out.println("Produtos exportados para " + fileName);
    }

}