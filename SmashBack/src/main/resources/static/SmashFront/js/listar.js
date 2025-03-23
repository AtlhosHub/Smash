addEventListener("load", listar())

function listar(){
    fetch(
        'http://localhost:8080/alunos/comprovantes',
        {
            method: 'GET',
        }
    ).then((res) => {
        console.log(res);
        return res.json()
    }).then((json) => {
        console.log(json);
        let alunos = ""
        json.forEach((coisa) => {
            let statusComprovante = coisa.status
            let classComprovante
            switch (statusComprovante) {
                case "EM ATRASO":
                    classComprovante = "atraso"
                    break;
            
                default:
                    break;
            }
            alunos+= `
                <tr class="a">
                    <td>
                        <svg width="16" height="16" viewBox="0 0 16 16" xmlns="http://www.w3.org/2000/svg">
                            <circle cx="8" cy="8" r="8" fill="${coisa.ativo ? "#9B9B9B" : "#444444"}" />
                        </svg>
                    </td>
                    <td><a href="./fichaAluno.html?id=${coisa.id}">${coisa.nome}</a></td>
                    <td>${coisa.dtEnvio != null ? coisa.dtEnvio : ""}</td>
                    <td class="status ${classComprovante}">${coisa.status}</td>
                </tr>
            `
        })
        tabela.innerHTML += alunos
    }).catch((error) => {
        console.error(error);
    })
}