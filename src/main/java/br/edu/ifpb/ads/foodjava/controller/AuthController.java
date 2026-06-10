package br.edu.ifpb.ads.foodjava.controller;

import br.edu.ifpb.ads.foodjava.exception.ArquivoImportacaoException;
import br.edu.ifpb.ads.foodjava.exception.DocumentoInvalidoException;
import br.edu.ifpb.ads.foodjava.exception.SenhaInvalidaException;
import br.edu.ifpb.ads.foodjava.exception.UsuarioDuplicadoException;
import br.edu.ifpb.ads.foodjava.model.Cliente;
import br.edu.ifpb.ads.foodjava.model.Gerente;
import br.edu.ifpb.ads.foodjava.model.Usuario;
import br.edu.ifpb.ads.foodjava.repository.ClienteRepository;
import br.edu.ifpb.ads.foodjava.util.ValidadorDocumento;
import br.edu.ifpb.ads.foodjava.util.ValidadorSenha;

public class AuthController {

    private final ClienteRepository clienteRepository = new ClienteRepository();
    private Gerente gerente;

    public void setGerente(Gerente gerente) {
        this.gerente = gerente;
    }

    public void cadastrarCliente(String nome, String email, String senha, String cpf, String telefone, String endereco)
            throws UsuarioDuplicadoException, SenhaInvalidaException, DocumentoInvalidoException, ArquivoImportacaoException {

        if (!ValidadorSenha.validar(senha))
            throw new SenhaInvalidaException("Senha deve ter ao menos 8 caracteres e um número");

        if (!ValidadorDocumento.validarCPF(cpf))
            throw new DocumentoInvalidoException("CPF inválido");

        if (clienteRepository.buscarPorEmail(email) != null)
            throw new UsuarioDuplicadoException("E-mail já cadastrado");

        if (clienteRepository.buscarPorCpf(cpf) != null)
            throw new UsuarioDuplicadoException("CPF já cadastrado");

        long id = System.currentTimeMillis();
        Cliente cliente = new Cliente(id, nome, email, senha, telefone, cpf, endereco);
        clienteRepository.salvarCliente(cliente);
    }

    public Usuario login(String email, String senha) throws Exception {
        if (gerente != null && gerente.getEmail().equalsIgnoreCase(email) && gerente.getSenha().equals(senha)) {
            return gerente;
        }

        Cliente cliente = clienteRepository.buscarPorEmail(email);
        if (cliente != null && cliente.getSenha().equals(senha)) {
            return cliente;
        }

        throw new Exception("E-mail ou senha incorretos");
    }
}