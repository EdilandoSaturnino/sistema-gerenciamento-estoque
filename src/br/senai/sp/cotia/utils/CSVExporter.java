package br.senai.sp.cotia.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import br.senai.sp.cotia.models.Cliente;
import br.senai.sp.cotia.models.Pedido;
import br.senai.sp.cotia.models.Produto;
import br.senai.sp.cotia.models.Vendedor;

public class CSVExporter {

    public static void exportData(List<Produto> produtos, List<Vendedor> vendedores, List<Cliente> clientes,
            List<Pedido> pedidos, String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            StringBuilder sb = new StringBuilder();

            appendSection(sb, "Produtos", "Nome,Categoria,Preco", produtos,
                    p -> new String[] { p.getNome(), p.getCategoria().toString(), String.valueOf(p.getPreco()) });
            appendSection(sb, "Vendedores", "Nome,Email,CPF,RG,Endereco", vendedores,
                    v -> new String[] { v.getNomeCompleto(), v.getEmail(), v.getCpf(), v.getRg(), v.getEndereco() });
            appendSection(sb, "Clientes", "CPF/CNPJ,Telefone,Nome,Email,Endereco", clientes,
                    c -> new String[] { c.getCpfCnpj(), c.getTelefone(), c.getNome(), c.getEmail(), c.getEndereco() });
            appendVendas(sb, pedidos, vendedores, clientes);

            writer.write(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static <T> void appendSection(StringBuilder sb, String title, String header, List<T> items,
            DataMapper<T> mapper) {
        sb.append("=== ").append(title).append(" ===\n").append(header).append("\n");
        for (T item : items) {
            StringJoiner joiner = new StringJoiner(",");
            for (String field : mapper.mapData(item)) {
                joiner.add(field != null ? field : "");
            }
            sb.append(joiner.toString()).append("\n");
        }
        sb.append("\n");
    }

    private static void appendVendas(StringBuilder sb, List<Pedido> pedidos, List<Vendedor> vendedores,
            List<Cliente> clientes) {
        sb.append("=== Vendas ===\nID Vendedor, ID Cliente, Produto, Quantidade\n");
        for (Pedido pedido : pedidos) {
            Vendedor vendedor = pedido.getVendedor();
            Cliente cliente = pedido.getCliente();
            for (Map.Entry<Produto, Integer> entry : pedido.getProdutos().entrySet()) {
                Produto produto = entry.getKey();
                Integer quantidade = entry.getValue();
                StringJoiner joiner = new StringJoiner(",");
                joiner.add(String.valueOf(vendedores.indexOf(vendedor) + 1))
                        .add(String.valueOf(clientes.indexOf(cliente) + 1))
                        .add(produto.getNome())
                        .add(String.valueOf(quantidade));
                sb.append(joiner.toString()).append("\n");
            }
        }
    }

    @FunctionalInterface
    private interface DataMapper<T> {
        String[] mapData(T item);
    }
}
