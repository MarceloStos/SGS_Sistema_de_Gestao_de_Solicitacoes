const API_BASE = '/';

const app = {
    currentId: null,

    navigate: (sectionId) => {
        document.querySelectorAll('.section').forEach(el => el.classList.remove('active'));
        document.getElementById(sectionId).classList.add('active');

        document.querySelectorAll('.sidebar .nav-link').forEach(el => el.classList.remove('active'));
        const navLink = Array.from(document.querySelectorAll('.sidebar .nav-link')).find(el => el.getAttribute('onclick').includes(sectionId));
        if (navLink) navLink.classList.add('active');

        app.hideAlert();

        // inicialização de cada tela
        if (sectionId === 'solicitacoes-list') app.loadSolicitacoes();
        if (sectionId === 'solicitantes-list') app.loadSolicitantes();
        if (sectionId === 'solicitacoes-form') app.loadDominios();
    },

    // Tratamento de Alertas e Erros
    showAlert: (msg, type = 'error') => {
        const box = document.getElementById('alert-box');
        box.innerText = msg;
        box.className = `alert alert-${type}`;
        box.style.display = 'block';
        setTimeout(() => app.hideAlert(), 5000);
    },
    hideAlert: () => {
        document.getElementById('alert-box').style.display = 'none';
    },

    novaSolicitacao: () => {
            app.navigate('solicitacoes-form'); // Navega para a tela
            // Limpa os campos
            document.getElementById('sol-id').value = '';
            document.getElementById('sol-descricao').value = '';
            document.getElementById('sol-valor').value = '';
            document.getElementById('sol-solicitante').disabled = false;
            document.getElementById('form-solicitacao-title').innerText = 'Nova Solicitação';
        },

        novoSolicitante: () => {
            app.navigate('solicitantes-form'); // Navega para a tela
             // Limpa os campos
            document.getElementById('solicitante-id').value = '';
            document.getElementById('solicitante-nome').value = '';
            document.getElementById('solicitante-documento').value = '';
            document.getElementById('form-solicitante-title').innerText = 'Novo Solicitante';
        },

    async fetchApi(url, options = {}) {
        try {
            const response = await fetch(url, options);
            if (!response.ok) {
                // Etrai a mensagem de erro
                let errorMsg = 'Ocorreu um erro na requisição.';
                const text = await response.text();
                try {
                    if (text) {
                        const errorData = JSON.parse(text);
                        if (errorData.message) errorMsg = errorData.message;
                        else if (errorData.erro) errorMsg = errorData.erro;
                        else if (errorData.errors) errorMsg = errorData.errors.join(', ');
                    }
                } catch (e) {
                    // Resposta não é JSON válido
                    if (text) errorMsg = text;
                }
                throw new Error(errorMsg);
            }
            // Retorna vazio em DELETE ou status 204
            if (response.status === 204) return null;
            return await response.json();
        } catch (error) {
            app.showAlert(error.message, 'error');
            throw error; // Repassa para quem chamou parar a execução
        }
    },

    // -------------------DOMÍNIOS--------------------

    async loadDominios() {
        try {
            const solicitantes = await app.fetchApi('/solicitantes');
            const categorias = await app.fetchApi('/categorias')
            const selSol = document.getElementById('sol-solicitante');
            selSol.innerHTML = '<option value="">Selecione...</option>';
            solicitantes.forEach(s => selSol.innerHTML += `<option value="${s.id}">${s.nome} (${s.cpfCnpj})</option>`);

            const selCat = document.getElementById('sol-categoria');
            const selFiltroCat = document.getElementById('filtro-categoria');


            selCat.innerHTML = '<option value="">Selecione...</option>';
            selFiltroCat.innerHTML = '<option value="">Todas as Categorias</option>';
            categorias.forEach(c => {
                selCat.innerHTML += `<option value="${c.id}">${c.nome}</option>`;
                selFiltroCat.innerHTML += `<option value="${c.id}">${c.nome}</option>`;
            });

        } catch (error) {
            console.error("Erro ao carregar domínios", error);
        }
    },

    // --------------------SOLICITAÇÕES-----------------------
    async loadSolicitacoes() {
        const status = document.getElementById('filtro-status').value;
        const catId = document.getElementById('filtro-categoria').value;
        const inicio = document.getElementById('filtro-inicio').value;
        const fim = document.getElementById('filtro-fim').value;

        let query = '?';
        if (status) query += `status=${status}&`;
        if (catId) query += `categoriaId=${catId}&`;
        if (inicio) query += `dataInicio=${inicio}&`;
        if (fim) query += `dataFim=${fim}`;

        try {
            const data = await app.fetchApi('/solicitacoes' + query);
            const tbody = document.getElementById('tbody-solicitacoes');
            tbody.innerHTML = '';

            data.forEach(s => {
            // Se for status final (Rejeutado ou Cancelado), desabilita o seletor
                let botoesStatus = '';

                if (s.status === 'SOLICITADO') {
                    botoesStatus += `<button class="btn btn-success" style="padding: 0.2rem 0.5rem; font-size: 0.8rem" onclick="app.changeStatusFromList(${s.id}, 'LIBERADO')">Liberar</button> `;
                    botoesStatus += `<button class="btn btn-danger" style="padding: 0.2rem 0.5rem; font-size: 0.8rem" onclick="app.changeStatusFromList(${s.id}, 'REJEITADO')">Rejeitar</button>`;
                }
                else if (s.status === 'LIBERADO') {
                    botoesStatus += `<button class="btn btn-success" style="padding: 0.2rem 0.5rem; font-size: 0.8rem" onclick="app.changeStatusFromList(${s.id}, 'APROVADO')">Aprovar</button> `;
                    botoesStatus += `<button class="btn btn-danger" style="padding: 0.2rem 0.5rem; font-size: 0.8rem" onclick="app.changeStatusFromList(${s.id}, 'REJEITADO')">Rejeitar</button>`;
                }
                else if (s.status === 'APROVADO') {
                    botoesStatus += `<button class="btn btn-danger" style="padding: 0.2rem 0.5rem; font-size: 0.8rem" onclick="app.changeStatusFromList(${s.id}, 'CANCELADO')">Cancelar</button>`;
                }
                tbody.innerHTML += `
                    <tr>
                        <td>#${s.id}</td>
                        <td>${s.solicitante.nome}</td>
                        <td>${s.solicitante.cpfCnpj}</td>
                        <td>${s.categoria.nome}</td>
                        <td>${new Date(s.dataSolicitacao).toLocaleDateString('pt-BR')}</td>
                        <td>R$ ${s.valor.toFixed(2)}</td>
                        <td><span class="badge badge-${s.status}">${s.status}</span></td>
                        <td style="display: flex; gap: 5px;">
                            ${botoesStatus}
                            <button class="btn btn-primary" style="padding: 0.2rem 0.5rem; font-size: 0.8rem" onclick="app.editSolicitacao(${s.id})">Editar</button>
                            <button class="btn btn-secondary" style="padding: 0.2rem 0.5rem; font-size: 0.8rem" onclick="app.viewSolicitacao(${s.id})">Detalhar</button>
                        </td>
                    </tr>
                `;
            });
        } catch (e) {
        console.error(e);
        }
    },

    async editSolicitacao(id) {
        try {
            const s = await app.fetchApi(`/solicitacoes/${id}`);

            await app.loadDominios();

            document.getElementById('sol-id').value = s.id;
            document.getElementById('sol-solicitante').value = s.solicitante.id
            document.getElementById('sol-categoria').value = s.categoria.id
            document.getElementById('sol-descricao').value = s.descricao;
            document.getElementById('sol-valor').value = s.valor;

            document.getElementById('form-solicitacao-title').innerText = `Editar Solicitação #${s.id}`;
            document.getElementById('sol-solicitante').disabled = true; // Bloqueia alterar o dono do pedido

            app.navigate('solicitacoes-form');
        } catch (e) {
            console.error("Erro ao carregar solicitação para edição:", e);
        }
    },

    async changeStatusFromList(id, novoStatus) {
        // Confirmação de seguranca
        if (!confirm(`Tem certeza que deseja mudar a solicitação #${id} para ${novoStatus}?`)) {
            return;
        }

        try {
            await app.fetchApi(`/solicitacoes/${id}/status`, {
                method: 'PATCH',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ status: novoStatus })
            });

            app.showAlert('Status atualizado com sucesso!', 'success');

            app.loadSolicitacoes();

        } catch (e) {
            app.showAlert(e.message, 'error');
        }
    },

    async saveSolicitacao(e) {
        e.preventDefault();
        const id = document.getElementById('sol-id').value;

        const payload = {
            solicitante: { id: parseInt(document.getElementById('sol-solicitante').value) },
            categoria: { id: parseInt(document.getElementById('sol-categoria').value) },
            descricao: document.getElementById('sol-descricao').value,
            valor: parseFloat(document.getElementById('sol-valor').value)
        };

        try {
            if (id) {
                // EDIÇÃO (PUT)
                await app.fetchApi(`/solicitacoes/${id}`, {
                    method: 'PUT',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(payload)
                });
                app.showAlert('Solicitação atualizada com sucesso!', 'success');
            } else {
                // CRIAÇAO (POST)
                await app.fetchApi('/solicitacoes', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(payload)
            });
            app.showAlert('Solicitação criada com sucesso!', 'success');
            }
            e.target.reset();
            app.navigate('solicitacoes-list');
        } catch (e) {}
    },

    async viewSolicitacao(id) {
        try {
            const s = await app.fetchApi(`/solicitacoes/${id}`);
            app.currentId = s.id;

            document.getElementById('det-id').innerText = s.id;
            document.getElementById('det-solicitante').innerText = `${s.solicitante.nome} (${s.solicitante.cpfCnpj})`;
            document.getElementById('det-categoria').innerText = s.categoria.nome;
            document.getElementById('det-descricao').innerText = s.descricao;
            document.getElementById('det-valor').innerText = `R$ ${s.valor.toFixed(2)}`;
            document.getElementById('det-data').innerText = new Date(s.dataSolicitacao).toLocaleString('pt-BR');
            document.getElementById('det-status').innerHTML = `<span class="badge badge-${s.status}">${s.status}</span>`;

            app.navigate('solicitacoes-view');
        } catch (e) {}
    },

    async updateStatus() {
        if (!app.currentId) return;
        const novoStatus = document.getElementById('novo-status').value;

        try {
            await app.fetchApi(`/solicitacoes/${app.currentId}/status`, {
                method: 'PATCH',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ status: novoStatus })
            });
            app.showAlert('Status atualizado com sucesso!', 'success');
            app.viewSolicitacao(app.currentId);
        } catch (e) {}
    },

    // -----------------------------SOLICITANTES------------------------------
    async loadSolicitantes() {
        try {
            const data = await app.fetchApi('/solicitantes');
            const tbody = document.getElementById('tbody-solicitantes');
            tbody.innerHTML = '';

            data.forEach(s => {
                tbody.innerHTML += `
                    <tr>
                        <td>#${s.id}</td>
                        <td>${s.nome}</td>
                        <td>${s.cpfCnpj}</td>
                        <td>
                            <button class="btn btn-secondary" onclick="app.editSolicitante(${s.id}, '${s.nome}', '${s.cpfCnpj}')">Editar</button>
                        </td>
                    </tr>
                `;
            });
        } catch (e) {}
    },

    editSolicitante(id, nome, documento) {
        document.getElementById('solicitante-id').value = id;
        document.getElementById('solicitante-nome').value = nome;
        document.getElementById('solicitante-documento').value = documento;
        document.getElementById('form-solicitante-title').innerText = 'Editar Solicitante';
        app.navigate('solicitantes-form');
    },

    async saveSolicitante(e) {
        e.preventDefault();
        const id = document.getElementById('solicitante-id').value;
        const payload = {
            nome: document.getElementById('solicitante-nome').value,
            cpfCnpj: document.getElementById('solicitante-documento').value
        };

        try {
            if (id) {
                // Atualizar
                await app.fetchApi(`/solicitantes/${id}`, {
                    method: 'PUT',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(payload)
                });
                app.showAlert('Solicitante atualizado com sucesso!', 'success');
            } else {
                // Criar
                await app.fetchApi('/solicitantes', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(payload)
                });
                app.showAlert('Solicitante criado com sucesso!', 'success');
            }
            app.navigate('solicitantes-list');
        } catch (e) {}
    }
};

window.onload = () => {
    app.loadDominios();
    app.navigate('solicitacoes-list');
};
