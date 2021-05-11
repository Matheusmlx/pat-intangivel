import {shallowMount} from '@vue/test-utils'
import NovaMovimentacao from './NovaMovimentacao'
import Vuex from 'vuex'
import {actionTypes, mutationTypes} from '@/core/constants'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import flushPromises from 'flush-promises'

describe('NovaMovimentacao.vue', () => {

    let localVue, wrapper, state, router, store, actions, mutations

    const movimentacao = {
        id: 1,
        tipo: 'TRANSFERENCIA_DEFINITIVA',
        idPatrimonio: 1,
        situacao: 'EM_ELABORACAO'
    }

    const orgaos = {
        items: [{id: 5, sigla: 'AGEREG', nome: 'Agência de Regulação dos Serviços Públicos Delegados de Campo Grande'}]
    }

    const resultado = {
        items: [
            {
                id: 1,
                tipo: 'TRANSFERENCIA_DEFINITIVA',
                situacao: 'EM_ELABORACAO',
                data: '11/11/1111',
                motivo: 'teste',
                orgaoDestino: {
                    codHierarquia: '0002.0005.0007',
                    descricao: 'AEM-MS - Agência Estadual de Metrologia',
                    id: 129,
                    nome: 'Agência Estadual de Metrologia',
                    sigla: 'AEM-MS'
                },
                orgaoOrigem: {
                    codHierarquia: '0002.0005.0007',
                    descricao: 'AEM-MS - Agência Estadual de Metrologia',
                    id: 129,
                    nome: 'Agência Estadual de Metrologia',
                    sigla: 'AEM-MS'
                }
            },
            {
                id: 2,
                tipo: 'DOACAO_ENTRE_ORGAOS',
                situacao: 'Finalizado',
                data: '11/11/1111',
                motivo: 'teste',
                orgaoDestino: {
                    codHierarquia: '0002.0005.0007',
                    descricao: 'AEM-MS - Agência Estadual de Metrologia',
                    id: 129,
                    nome: 'Agência Estadual de Metrologia',
                    sigla: 'AEM-MS'
                },
                orgaoOrigem: {
                    codHierarquia: '0002.0005.0007',
                    descricao: 'AEM-MS - Agência Estadual de Metrologia',
                    id: 129,
                    nome: 'Agência Estadual de Metrologia',
                    sigla: 'AEM-MS'
                }
            }
        ]
    }

    beforeEach(() => {
        localVue = applicationTestBuilder.build()

        state = {
            movimentacao: {
                movimentacao: {}
            },
            rota: {
                origem: 'Inicial'
            }
        }

        router = {
            init: jest.fn(),
            push: jest.fn(),
            replace: jest.fn(),
            history: {
                current: {
                    name: 'MenuDetalhe',
                    params: {
                        id: 1
                    }
                }
            }
        }


        actions = {
            [actionTypes.MOVIMENTACAO.ATUALIZAR_MOVIMENTACAO]: jest.fn(),
            [actionTypes.MOVIMENTACAO.ENVIAR_MOVIMENTACAO]: jest.fn(),
            [actionTypes.MOVIMENTACAO.DELETAR_MOVIMENTACAO]: jest.fn(),
            [actionTypes.MOVIMENTACAO.BUSCAR_MOVIMENTACAO_POR_PATRIMONIO]: jest.fn().mockReturnValue(resultado),
            [actionTypes.MOVIMENTACAO.CADASTRAR_MOVIMENTACAO]: jest.fn().mockReturnValue(movimentacao),
            [actionTypes.MOVIMENTACAO.BUSCAR_ORGAOS_DESTINO]: jest.fn().mockReturnValue(orgaos)
        }

        mutations = {
            [mutationTypes.MOVIMENTACAO.SET_MOVIMENTACAO]: jest.fn(),
            [mutationTypes.LOKI.DISABLE_GLOBAL_LOADING]: jest.fn(),
            [mutationTypes.LOKI.ENABLE_GLOBAL_LOADING]: jest.fn()
        }

        store = new Vuex.Store({state, actions, mutations})
    })

    describe('methods', () => {

        it('deve buscar movimentações', async () => {
            wrapper = shallowMount(NovaMovimentacao, {
                localVue,
                router,
                store
            })
            await wrapper.vm.buscarMovimentacoes()

            await flushPromises()

            expect(actions[actionTypes.MOVIMENTACAO.BUSCAR_MOVIMENTACAO_POR_PATRIMONIO]).toHaveBeenCalled()
            expect(actions[actionTypes.MOVIMENTACAO.BUSCAR_MOVIMENTACAO_POR_PATRIMONIO].mock.calls[0][1]).toEqual({idPatrimonio: 1})
            expect(wrapper.vm.exibirLinhaDoTempo).toEqual(true)
        })

        it('deve setar exibirLinhaDoTempo como false caso não há movimentações para o patrimônio', async () => {
            const resultadoSemItems = {items :[]}
            actions[actionTypes.MOVIMENTACAO.BUSCAR_MOVIMENTACAO_POR_PATRIMONIO] = jest.fn().mockReturnValue(resultadoSemItems)

            wrapper = shallowMount(NovaMovimentacao, {
                localVue,
                router,
                store: new Vuex.Store({state, mutations, actions}),
            })
            await flushPromises
            await wrapper.vm.buscarMovimentacoes()

            await flushPromises()
            expect(actions[actionTypes.MOVIMENTACAO.BUSCAR_MOVIMENTACAO_POR_PATRIMONIO]).toHaveBeenCalled()
            expect(actions[actionTypes.MOVIMENTACAO.BUSCAR_MOVIMENTACAO_POR_PATRIMONIO].mock.calls[0][1]).toEqual({idPatrimonio: 1})
            expect(wrapper.vm.exibirLinhaDoTempo).toEqual(false)
        })

        it('deve atualizar a movimentação', async () => {
            wrapper = shallowMount(NovaMovimentacao, {
                localVue,
                router,
                store
            })
            await wrapper.vm.atualizarMovimentacao(movimentacao)

            await flushPromises()

            expect(actions[actionTypes.MOVIMENTACAO.ATUALIZAR_MOVIMENTACAO]).toHaveBeenCalled()
            expect(actions[actionTypes.MOVIMENTACAO.ATUALIZAR_MOVIMENTACAO].mock.calls[0][1]).toEqual(movimentacao)
        })

        it('deve cadastrar a movimentação', async () => {
            const movimentacaoCadastrar = {
                tipo: 'TRANSFERENCIA_DEFINITIVA',
                idPatrimonio: 1
            }
            const tipo = 'TRANSFERENCIA_DEFINITIVA'
            wrapper = shallowMount(NovaMovimentacao, {
                localVue,
                router,
                store
            })
            await wrapper.vm.cadastrarMovimentacao(tipo)

            await flushPromises()

            expect(actions[actionTypes.MOVIMENTACAO.CADASTRAR_MOVIMENTACAO]).toHaveBeenCalled()
            expect(actions[actionTypes.MOVIMENTACAO.CADASTRAR_MOVIMENTACAO].mock.calls[0][1]).toEqual(movimentacaoCadastrar)
        })

        it('deve enviar uma movimentação', async () => {
            wrapper = shallowMount(NovaMovimentacao, {
                localVue,
                router,
                store
            })
            await wrapper.vm.enviarMovimentacao()

            expect(actions[actionTypes.MOVIMENTACAO.ENVIAR_MOVIMENTACAO]).toHaveBeenCalled()
        })

        it('deve retornar true quando houver uma movimentação com a situação Finalizado', async () => {
            wrapper = shallowMount(NovaMovimentacao, {
                localVue,
                router,
                store
            })
            await flushPromises
            await wrapper.vm.deveExibirTooltipMovimentacao()

            await flushPromises
            expect(wrapper.vm.exibirTooltipMovimentacao).toEqual(true)
        })

        it('deve remover uma movimentação', async () => {
            wrapper = shallowMount(NovaMovimentacao, {
                localVue,
                router,
                store
            })
            wrapper.setData({
                movimentacao:{
                    id: 1
                }
            })
            await flushPromises
            await wrapper.vm.tratarEventoDeletarMovimentacao()

            await flushPromises
            expect(actions[actionTypes.MOVIMENTACAO.DELETAR_MOVIMENTACAO]).toHaveBeenCalled()
            expect(actions[actionTypes.MOVIMENTACAO.DELETAR_MOVIMENTACAO].mock.calls[0][1]).toEqual(wrapper.vm.$data.movimentacao.id)
        })

        it('deve setar a situação da movimentação de acordo com o parâmetro recebido', () => {
            wrapper = shallowMount(NovaMovimentacao, {
                localVue,
                router,
                store
            })

            wrapper.vm.setarSituacaoMovimentacao(movimentacao)

            expect(wrapper.vm.situacao).toEqual('EM_ELABORACAO')
        })
    })
})
