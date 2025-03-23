document.querySelector(".save").addEventListener("click", async function (event) {
    event.preventDefault();

    const formData = {
        nome: document.querySelector("#nome").value,
        nacionalidade: document.querySelector("#nacionalidade").value,
        naturalidade: document.querySelector("#naturalidade").value,
        dataNascimento: document.querySelector("#dataNascimento").value,
        endereco:{
            logradouro: document.querySelector("#residencia").value,
            numLogradouro: document.querySelector("#numero").value,
            bairro: document.querySelector("#bairro").value,
            cidade: document.querySelector("#cidade").value,
            cep: document.querySelector("#cep").value,
        },
        telefone: document.querySelector("#telefone").value,
        celular: document.querySelector("#celular").value,
        email: document.querySelector("#email").value,
        profissao: document.querySelector("#profissao").value,
        rg: document.querySelector("#rg").value,
        cpf: document.querySelector("#cpf").value,
        ativo: document.querySelector("#presencaTrue:checked")?.value || "false",
        temAtestado: document.querySelector("#atestadoTrue:checked")?.value || "false",
        temAssinatura: document.querySelector("#assinaturaTrue:checked")?.value || "false",
        autorizado: document.querySelector("#autorizacaoTrue:checked")?.value || "false",
    };

    try {
        const response = await fetch("http://localhost:8080/alunos", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(formData)
        });

        if (response.ok) {
            alert("Cadastro realizado com sucesso!");
            document.querySelector("form").reset();
        } else if (response.status == 409) {
            alert("Email, RG ou CPF já cadastrados");
        } else {
            alert("Erro ao cadastrar. Tente novamente.");
        }
    } catch (error) {
        console.error("Erro ao enviar dados: ", error);
        alert("Falha na conexão com a API.");
    }
});

document.querySelector(".cancel").addEventListener("click", function (event) {
    event.preventDefault();
    window.location = "./index.html"
});

document.querySelector(".apresentacao").addEventListener("click", function (event) {
    event.preventDefault();

    const formData = {
        nome: "João Silva",
        nacionalidade: "Brasileira",
        naturalidade: "São Paulo",
        dataNascimento: "2005-07-15",
        endereco: {
            logradouro: "Rua das Flores",
            numLogradouro: "123",
            bairro: "Centro",
            cidade: "São Paulo",
            cep: "01010010"
        },
        telefone: "1140028922",
        celular: "11999991234",
        email: "joao.silva@email.com",
        profissao: "Estudante",
        rg: "456789012",
        cpf: "12345678909",
        ativo: true,
        temAtestado: true,
        temAssinatura: true,
        autorizado: false
    };

    document.querySelector("#nome").value = formData.nome
    document.querySelector("#nacionalidade").value = formData.nacionalidade
    document.querySelector("#naturalidade").value = formData.naturalidade
    document.querySelector("#dataNascimento").value = formData.dataNascimento
    document.querySelector("#residencia").value = formData.endereco.logradouro
    document.querySelector("#numero").value = formData.endereco.numLogradouro
    document.querySelector("#bairro").value = formData.endereco.bairro
    document.querySelector("#cidade").value = formData.endereco.cidade
    document.querySelector("#cep").value = formData.endereco.cep
    document.querySelector("#telefone").value = formData.telefone
    document.querySelector("#celular").value = formData.celular
    document.querySelector("#email").value = formData.email
    document.querySelector("#profissao").value = formData.profissao
    document.querySelector("#rg").value = formData.rg
    document.querySelector("#cpf").value = formData.cpf
    document.querySelector(`#presenca${formData.ativo ? "True" : "False"}`).checked = true;
    document.querySelector(`#atestado${formData.temAtestado ? "True" : "False"}`).checked = true;
    document.querySelector(`#assinatura${formData.temAssinatura ? "True" : "False"}`).checked = true;
    document.querySelector(`#autorizacao${formData.autorizado ? "True" : "False"}`).checked = true;
})