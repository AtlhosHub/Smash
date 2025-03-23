let params = new URLSearchParams(document.location.search);
let id = params.get("id");

document.addEventListener("load", dados(id))
document.querySelector(".cancel").addEventListener("click", (event) => {
    event.preventDefault()
    window.location = "./index.html"
});

document.querySelector(".save").addEventListener("click", (event) => {
    event.preventDefault()
    editar(id)
});

function dados(id) {
    fetch(
        'http://localhost:8080/alunos/' + id,
        {
            method: 'GET',
        }
    ).then((res) => {
        console.log(res);
        return res.json()
    }).then((json) => {
        document.querySelector("#nome").value = json.nome
        document.querySelector("#nacionalidade").value = json.nacionalidade
        document.querySelector("#naturalidade").value = json.naturalidade
        document.querySelector("#dataNascimento").value = json.dataNascimento
        document.querySelector("#residencia").value = json.endereco.logradouro
        document.querySelector("#numero").value = json.endereco.numLogradouro
        document.querySelector("#bairro").value = json.endereco.bairro
        document.querySelector("#cidade").value = json.endereco.cidade
        document.querySelector("#cep").value = json.endereco.cep
        document.querySelector("#telefone").value = json.telefone
        document.querySelector("#celular").value = json.celular
        document.querySelector("#email").value = json.email
        document.querySelector("#profissao").value = json.profissao
        document.querySelector("#rg").value = json.rg
        document.querySelector("#cpf").value = json.cpf
        document.querySelector(`#presenca${json.ativo ? "True" : "False"}`).checked = true;
        document.querySelector(`#atestado${json.temAtestado ? "True" : "False"}`).checked = true;
        document.querySelector(`#assinatura${json.temAssinatura ? "True" : "False"}`).checked = true;
        document.querySelector(`#autorizacao${json.autorizado ? "True" : "False"}`).checked = true;
    }).catch((error) => {
        console.error(error);
    })
}

async function editar(id){
    if (confirm("Tem certeza que deseja editar esse usuário?")) {
        const formData = {
            nome: document.querySelector("#nome").value,
            nacionalidade: document.querySelector("#nacionalidade").value,
            naturalidade: document.querySelector("#naturalidade").value,
            dataNascimento: document.querySelector("#dataNascimento").value,
            endereco: {
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
            const response = await fetch("http://localhost:8080/alunos/"+id, {
                method: "PUT",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(formData)
            });
    
            if (response.ok) {
                alert("Aluno editado com sucesso!");
                window.location = "./fichaAluno.html?id="+id
            } else if (response.status == 409) {
                alert("Email, RG ou CPF já cadastrados");
            } else {
                alert("Erro ao editar. Tente novamente.");
            }
        } catch (error) {
            console.error("Erro ao enviar dados: ", error);
            alert("Falha na conexão com a API.");
        }
    }
}