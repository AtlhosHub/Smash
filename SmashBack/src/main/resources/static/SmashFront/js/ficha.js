let params = new URLSearchParams(document.location.search);
let id = params.get("id");

document.addEventListener("load", ficha(id))
document.querySelector(".delete").addEventListener("click", () => { excluir(id) })
document.querySelector(".edit").addEventListener("click", () => { editar(id) })

const nomeAtleta = document.getElementById("nomeAtleta")
const nacionalidade = document.getElementById("nacionalidade")
const naturalidade = document.getElementById("naturalidade")
const dataNascimento = document.getElementById("dataNascimento")
const responsavelUm = document.getElementById("responsavelUm")
const responsavelDois = document.getElementById("responsavelDois")
const logradouro = document.getElementById("logradouro")
const bairro = document.getElementById("bairro")
const cidade = document.getElementById("cidade")
const cep = document.getElementById("cep")
const telefone = document.getElementById("telefone")
const celular = document.getElementById("celular")
const email = document.getElementById("email")
const profissao = document.getElementById("profissao")
const rg = document.getElementById("rg")
const cpf = document.getElementById("cpf")
const presenca = document.getElementById("presenca")
const atestado = document.getElementById("atestado")
const assinatura = document.getElementById("assinatura")
const autorizacao = document.getElementById("autorizacao")

function ficha(id) {
    fetch(
        'http://localhost:8080/alunos/'+id,
        {
            method: 'GET',
        }
    ).then((res) => {
        console.log(res);
        return res.json()
    }).then((json) => {
        console.log(json);
        nomeAtleta.innerText = json.nome
        nacionalidade.innerText = json.nacionalidade
        naturalidade.innerText = json.naturalidade
        dataNascimento.innerText = formatarData(json.dataNascimento);
        responsavelUm.innerText = json.responsaveis.length >= 1 && json.responsaveis[0] != null ? json.responsaveis[0].nome : "Não registrado"
        responsavelDois.innerText = json.responsaveis.length == 2 && json.responsaveis[1] != null ? json.responsaveis[1].nome : "Não registrado"
        logradouro.innerText = json.endereco.logradouro
        bairro.innerText = json.endereco.bairro
        cidade.innerText = json.endereco.cidade
        cep.innerText = json.endereco.cep
        telefone.innerText = json.telefone
        celular.innerText = json.celular
        email.innerText = json.email
        profissao.innerText = json.profissao
        rg.innerText = json.rg
        cpf.innerText = json.cpf
        presenca.innerText = json.ativo ? "Ativo" : "Inativo"
        atestado.innerText = json.temAtestado ? "Sim" : "Não"
        assinatura.innerText = json.temAssinatura ? "Sim" : "Não"
        autorizacao.innerText = json.autorizado ? "Sim" : "Não"
        return json
    }).catch((error) => {
        console.error(error);
    })
}

function formatarData(data) {
    if (!data) return "";
    let partes = data.split("-");
    return `${partes[2]}/${partes[1]}/${partes[0]}`;
}


function excluir(id){
    if (confirm("Tem certeza que deseja excluir esse usuário?")) {
        fetch(
            `http://localhost:8080/alunos/${id}`,
            {
                method: "DELETE"
            }
        ).then((res) => {
            console.log(res);
            alert("Aluno removido com sucesso!")
            window.location = "./listarAlunos.html"
        })
        .catch((error) => {
            console.error(error);
        })
    }
}

function editar(id){
    window.location = "./editarAluno.html?id="+id
}