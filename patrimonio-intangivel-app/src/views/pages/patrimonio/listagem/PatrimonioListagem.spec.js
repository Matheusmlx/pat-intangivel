import {shallowMount} from '@vue/test-utils'
import Vuex from 'vuex'
import flushPromises from 'flush-promises'
import {actionTypes, mutationTypes} from '@/core/constants'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import PatrimonioListagem from './PatrimonioListagem'

let criarStore = ({state, getters, mutations, actions, modules}) => {
    return new Vuex.Store({state, getters, mutations, actions, modules})
}

describe('PatrimonioListagem', () => {
    let wrapper, mutations, actions, localVue, router, state, store

    const patrimonio = {
        id: 0,
        numero: '48459875',
        nome: 'Adobe Illustrator 2020',
        situacao: 'Em desenvolvimento',
        tipo: 'SOFTWARES'
    }

    const patrimonioCompletoEmElaboracao = {
        id: 48,
        nome: 'teste',
        descricao: 'descricao',
        situacao: 'EM_ELABORACAO',
        reconhecimento: 'AQUISICAO_SEPARADA',
        dataAquisicao: '2020-01-13 03:00:00.000000',
        valorAquisicao: '10000',
        dataNL: '2020-01-13 03:00:00.000000',
        numeroNL: '12314NL12365',
        fornecedor: 1,
        dataVencimento: '2020-01-13 03:00:00.000000',
        dataAtivacao: '2020-01-13 03:00:00.000000',
        tipo: 'DIREITOS_AUTORAIS',
        estado: 'EM_DESENVOLVIMENTO',
        orgao: {
            id: 0,
            sigla: 'AGETEC',
            nome: 'Agência Municipal de Tecnologia da Informação e Inovação',
        },
        setor: {
            id: 0,
            sigla: 'DAS',
            nome: 'Diretoria de Atendimento e Suporte'
        },
        contaContabil: 1
    }

    const patrimonioCompletoAtivo = {
        id: 48,
        nome: 'teste',
        descricao: 'descricao',
        situacao: 'ATIVO',
        reconhecimento: 'AQUISICAO_SEPARADA',
        dataAquisicao: '2020-01-13 03:00:00.000000',
        valorAquisicao: '10000',
        dataNL: '2020-01-13 03:00:00.000000',
        numeroNL: '12314NL12365',
        fornecedor: 1,
        dataVencimento: '2020-01-13 03:00:00.000000',
        dataAtivacao: '2020-01-13 03:00:00.000000',
        tipo: 'DIREITOS_AUTORAIS',
        estado: 'EM_DESENVOLVIMENTO',
        orgao: {
            id: 0,
            sigla: 'AGETEC',
            nome: 'Agência Municipal de Tecnologia da Informação e Inovação',
        },
        setor: {
            id: 0,
            sigla: 'DAS',
            nome: 'Diretoria de Atendimento e Suporte'
        },
        contaContabil: 1
    }

    const modules = (valid = false) => ({
        CamposPersonalizados: {
            namespaced: true,
            getters: { getObjetoValidado: () => () => valid },
            actions: { getAllCamposPersonlizados: jest.fn() }
        }
    })

    beforeEach(() => {
        localVue = applicationTestBuilder.build()

        router = {
            init: jest.fn(),
            push: jest.fn(),
            history: {current: {name: 'TodosPatrimoniosTabela'}},

        }

        state = {
            loki: {
                user: {
                    domainId: 1,
                    type: 'INTERNO',
                    authorities: [
                        {
                            hasAccess: true,
                            name: 'Patrimonio.Normal'
                        }
                    ]
                }
            },
            patrimonio: {
                todosPatrimonios: {
                    paginacao: {
                        page: 0, rowsPerPage: 10, descending: false
                    },
                    filtros: {
                        conteudo: {},
                        objeto: {
                            value: null,
                            default: null,
                            label: 'Pesquisa'
                        },
                        tipoPatrimonio: {
                            value: null,
                            default: null,
                            label: 'Pesquisa'
                        }
                    }
                },
                isLista: true,
            }
        }

        mutations = {
            [mutationTypes.PATRIMONIO.SET_FILTROS_BUSCA_TODOS_PATRIMONIOS]: jest.fn(),
            [mutationTypes.PATRIMONIO.SET_PAGINACAO_BUSCA_TODOS_PATRIMONIOS]: jest.fn(),
            [mutationTypes.LOKI.SET_LOADING_MESSAGE]: jest.fn(),
            [mutationTypes.LOKI.SHOW_ALERT]: jest.fn(),
            [mutationTypes.PATRIMONIO.RESETA_PAGE]: jest.fn(),
            [mutationTypes.PATRIMONIO.SET_VISUALIZACAO_LISTAGEM]: jest.fn()

        }

        actions = {
            [actionTypes.PATRIMONIO.BUSCAR_TODOS_PATRIMONIOS]: jest.fn().mockReturnValue(patrimonio),
            [actionTypes.PATRIMONIO.BUSCAR_PATRIMONIO_POR_ID]: jest.fn()
        }

        store = new Vuex.Store({actions, state, mutations})
    })

    describe('Events', () => {
        it('Deve emitir o evento de paginar quando alterar a paginação', async () => {
            wrapper = shallowMount(PatrimonioListagem, {
                localVue,
                router,
                store: criarStore({state, mutations, actions}),
                stubs: {
                    PatrimonioListagemTabela: '<div><button class="stub" @click="$emit(\'paginar\', {})"></button></div>'
                }
            })

            wrapper.find('button[class="stub"]').trigger('click')
            await flushPromises()

            expect(mutations[mutationTypes.PATRIMONIO.SET_PAGINACAO_BUSCA_TODOS_PATRIMONIOS].mock.calls[0][1]).toEqual({})
            expect(actions[actionTypes.PATRIMONIO.BUSCAR_TODOS_PATRIMONIOS]).toHaveBeenCalled()
        })

        it('Deve emitir o evendo de buscar simples', async () => {
            wrapper = shallowMount(PatrimonioListagem, {
                localVue,
                router,
                store: criarStore({state, mutations, actions}),
                stubs: {
                    'az-search': '<div><button class="stub" @click="$emit(\'simple-search\', \'Aquisição\')"></button></div>'
                }
            })

            wrapper.find('button[class="stub"]').trigger('click')
            await flushPromises()

            expect(mutations[mutationTypes.PATRIMONIO.RESETA_PAGE]).toHaveBeenCalled()
            expect(mutations[mutationTypes.PATRIMONIO.SET_FILTROS_BUSCA_TODOS_PATRIMONIOS].mock.calls[0][1].conteudo.value).toEqual('Aquisição')
            expect(actions[actionTypes.PATRIMONIO.BUSCAR_TODOS_PATRIMONIOS]).toHaveBeenCalled()
            expect(wrapper.vm.filtrosInterno.conteudo.value).toEqual('Aquisição')
        })

        it('Deve filtrar os dados da tabela', async () => {
            wrapper = shallowMount(PatrimonioListagem, {
                localVue,
                router,
                store: criarStore({state, mutations, actions}),
                stubs: {
                    'az-search': '<div><button class="stub" @click="$emit(\'simple-search\', \'\')"></button></div>'
                }
            })

            wrapper.find('button[class="stub"]').trigger('click')
            await flushPromises()

            expect(mutations[mutationTypes.PATRIMONIO.RESETA_PAGE]).toHaveBeenCalled()
            expect(mutations[mutationTypes.PATRIMONIO.SET_FILTROS_BUSCA_TODOS_PATRIMONIOS].mock.calls[0][1].conteudo.value).toEqual('')
            expect(actions[actionTypes.PATRIMONIO.BUSCAR_TODOS_PATRIMONIOS]).toHaveBeenCalled()
            expect(wrapper.vm.filtrosInterno.conteudo.value).toEqual('')
        })

        xit('Deve emitir o evento de remover o filtro da busca simples', async () => {
            wrapper = shallowMount(PatrimonioListagem, {
                localVue,
                router,
                store: criarStore({state, mutations, actions}),
                stubs: {
                    'az-search': '<div><button class="stub" @click="$emit(\'remove-filter\', \'objeto\')"></button></div>'
                }
            })

            wrapper.setData({
                filtrosInterno: {
                    objeto: {
                        value: 'Folha',
                        default: 'Folha de pagamento',
                        label: 'Pesquisa'
                    }
                }
            })

            wrapper.find('button[class="stub"]').trigger('click')
            await flushPromises()

            expect(mutations[mutationTypes.PATRIMONIO.SET_FILTROS_BUSCA_TODOS_PATRIMONIOS].mock.calls[0][1].objeto.value).toEqual('Folha de pagamento')
            expect(actions[actionTypes.PATRIMONIO.BUSCAR_TODOS_PATRIMONIOS]).toHaveBeenCalled()
            expect(wrapper.vm.filtrosInterno.objeto.value).toEqual('Folha de pagamento')
        })

        it('Deve emitir o evento de acessar a patrimonio com a situação "Em Elaboração" sem campos obrigatorios preenchidos', async () => {
            actions = {
                [actionTypes.PATRIMONIO.BUSCAR_TODOS_PATRIMONIOS]: jest.fn().mockReturnValue(patrimonio),
                [actionTypes.PATRIMONIO.BUSCAR_PATRIMONIO_POR_ID]: jest.fn().mockReturnValue(patrimonioCompletoEmElaboracao)
            }
            state.loki.user.authorities = [
                {
                    hasAccess: true,
                    name: 'Comprador.Admin'
                }
            ]
            wrapper = shallowMount(PatrimonioListagem, {
                localVue,
                router,
                store: criarStore({
                    state,
                    mutations,
                    actions,
                    modules: modules()
                }),
                stubs: {
                    PatrimonioListagemTabela: '<div><button class="stub" @click="$emit(\'acessar\', patrimonio = {\n' +
                        '        id:0,\n' +
                        '        numero:\'--\',\n' +
                        '        nome: \'Adobe Illustrator 2020\',\n' +
                        '        situacao: \'Em desenvolvimento\',\n' +
                        '        estruturaorganizacional: \'SEJUSP\',\n' +
                        '\n' +
                        '    })"></button></div>'
                }
            })
            wrapper.find('button[class="stub"]').trigger('click')

            await flushPromises()
            expect(router.push.mock.calls[0][0]).toEqual({name: 'DadosDeEntradaEdicao', params: {id: 0}})
        })

        it('Deve emitir o evento de acessar a patrimonio com a situação "Em Elaboração" com todos campos obrigatorios preenchidos', async () => {
            actions = {
                [actionTypes.PATRIMONIO.BUSCAR_TODOS_PATRIMONIOS]: jest.fn().mockReturnValue(patrimonio),
                [actionTypes.PATRIMONIO.BUSCAR_PATRIMONIO_POR_ID]: jest.fn().mockReturnValue(patrimonioCompletoEmElaboracao)
            }
            state.loki.user.authorities = [
                {
                    hasAccess: true,
                    name: 'Comprador.Admin'
                }
            ]
            wrapper = shallowMount(PatrimonioListagem, {
                localVue,
                router,
                store: criarStore({
                    state,
                    mutations,
                    actions,
                    modules: modules(true)
                }),
                stubs: {
                    PatrimonioListagemTabela: '<div><button class="stub" @click="$emit(\'acessar\', patrimonio = {\n' +
                        '        id:0,\n' +
                        '        numero:\'--\',\n' +
                        '        nome: \'Adobe Illustrator 2020\',\n' +
                        '        situacao: \'Em desenvolvimento\',\n' +
                        '        estruturaorganizacional: \'SEJUSP\',\n' +
                        '\n' +
                        '    })"></button></div>'
                }
            })
            wrapper.find('button[class="stub"]').trigger('click')

            await flushPromises()
            expect(router.push.mock.calls[0][0]).toEqual({name: 'DocumentosEdicao', params: {id: 0}})
        })

        it('Deve emitir o evento de acessar a patrimonio com qualquer situação "Ativo"', async () => {
            actions = {
                [actionTypes.PATRIMONIO.BUSCAR_TODOS_PATRIMONIOS]: jest.fn().mockReturnValue(patrimonio),
                [actionTypes.PATRIMONIO.BUSCAR_PATRIMONIO_POR_ID]: jest.fn().mockReturnValue(patrimonioCompletoAtivo)
            }
            wrapper = shallowMount(PatrimonioListagem, {
                localVue,
                router,
                store: criarStore({state, mutations, actions, modules: modules(true)}),
                stubs: {
                    PatrimonioListagemTabela: '<div><button class="stub" @click="$emit(\'acessar\', patrimonio = {\n' +
                        '        id:0,\n' +
                        '        numero:\'--\',\n' +
                        '        nome: \'Adobe Illustrator 2020\',\n' +
                        '        situacao: \'ATIVO\',\n' +
                        '        estruturaorganizacional: \'SEJUSP\',\n' +
                        '\n' +
                        '    })"></button></div>'
                }
            })
            wrapper.find('button[class="stub"]').trigger('click')

            await flushPromises()
            expect(router.push.mock.calls[0][0]).toEqual({name: 'FichaPatrimonio', params: {id: 0}})
        })
    })

    describe('Methods', () => {
        it('Deve buscar todos os patrimonios', async () => {
            wrapper = shallowMount(PatrimonioListagem, {
                localVue,
                router,
                store: criarStore({state, mutations, actions})
            })
            await flushPromises()
            expect(actions[actionTypes.PATRIMONIO.BUSCAR_TODOS_PATRIMONIOS]).toHaveBeenCalled()
        })

        it('Deve mudar o tipo de visualização entre grade e lista', async () => {
            wrapper = shallowMount(PatrimonioListagem, {
                localVue,
                router,
                store: criarStore({state, mutations, actions})
            })

            const isLista = store.state.patrimonio.isLista
            wrapper.vm.tratarEventoListaOuGrade()
            await flushPromises()

            expect(mutations[mutationTypes.PATRIMONIO.SET_VISUALIZACAO_LISTAGEM].mock.calls[0][1]).toEqual(!isLista)
        })

        it('Deve redirecionar para tela de criação de um novo patrimonio', () => {
            wrapper = shallowMount(PatrimonioListagem, {
                localVue,
                router,
                store: criarStore({state, mutations, actions})
            })

            wrapper.vm.tratarEventoNovo()

            expect(router.push.mock.calls[0][0]).toEqual({name: 'TipoNovo'})
        })

        it('Deve falhar ao buscar as informações da busca', async () => {
            actions[actionTypes.PATRIMONIO.BUSCAR_TODOS_PATRIMONIOS] = jest.fn().mockRejectedValue(new Error('Ocorreu um erro ao realizar a operação.'))

            wrapper = shallowMount(PatrimonioListagem, {
                localVue,
                router,
                store: criarStore({state, mutations, actions})
            })

            await expect(wrapper.vm.buscaTodosPatrimonios()).rejects.toThrow('Ocorreu um erro ao realizar a operação.')
        })
    })
})
