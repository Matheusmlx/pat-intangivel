import { shallowMount } from '@vue/test-utils'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import { actionTypes, mutationTypes } from '@/core/constants'
import Vuex from 'vuex'
import flushPromises from 'flush-promises'
import PatrimonioEdicao from './PatrimonioEdicao'

describe('PatrimonioEdicao', () => {
    let wrapper, localVue, router, state, mutations, actions, errors

    const patrimonio = {
        id: 48,
        nome: null,
        descricao: null,
        situacao: null,
        reconhecimento: null,
        dataAquisicao: null,
        dataNL: null,
        numeroNL: null,
        fornecedor: null,
        dataVencimento: null,
        tipo: 'DIREITOS_AUTORAIS',
        estado: null,
        setorId: null,
        orgao: 1
    }

    const passo = {
        titulo: 'Bem intangível',
        rotaPadrao: 'TipoVisualizacao',
        rotaNovo: 'TipoNovo',
        rotaEdicao: 'TipoEdicao',
        desabilitado: false,
        numero: 1
    }

    beforeEach(() => {
        localVue = applicationTestBuilder.build()

        errors = {
            collect: jest.fn()
        }

        state = {
            loki: {
                user: {
                    domainId: 1,
                    type: 'INTERNO',
                }
            },
            patrimonio: {
                tipo: null
            },
            rota: {
                origem: 'Inicial'
            },
            salvamentoAutomatico: {
                salvando: false
            }
        }

        actions = {
            [actionTypes.PATRIMONIO.CADASTRAR_PATRIMONIO]: jest.fn(),
            [actionTypes.PATRIMONIO.SET_PATRIMONIO]: jest.fn(),
            [actionTypes.PATRIMONIO.ATUALIZAR_PATRIMONIO]: jest.fn(),
            [actionTypes.PATRIMONIO.SET_PATRIMONIO_BACKUP]: jest.fn(),
            [actionTypes.PATRIMONIO.DELETAR_PATRIMONIO]: jest.fn(),
            [actionTypes.PATRIMONIO.DELETAR_PATRIMONIO_NAO_ALTERADO]: jest.fn(),
            [actionTypes.PATRIMONIO.BUSCAR_PATRIMONIO_POR_ID]: jest.fn()
        }

        mutations = {
            [mutationTypes.LOKI.SET_LOADING_MESSAGE]: jest.fn(),
            [mutationTypes.PATRIMONIO.SET_PATRIMONIO]: jest.fn(),
            [mutationTypes.COMUM.SET_RETRAIR_MENU]: jest.fn(),
            [mutationTypes.COMUM.SET_EXPANDIR_MENU]: jest.fn(),
            [mutationTypes.LOKI.SHOW_ALERT]: jest.fn()
        }

        router = {
            init: jest.fn(),
            push: jest.fn(),
            replace: jest.fn(),
            history: {
                current: {
                    name: 'TipoEdicao',
                    params: { id: patrimonio.id }
                }
            }
        }

    })

    describe('Mouted', () => {

        it('Deve retrair o menu lateral', () => {
            wrapper = shallowMount(PatrimonioEdicao, {
                localVue,
                router,
                store: new Vuex.Store({ state, mutations, actions }),
                mocks: {
                    errors
                }
            })

            expect(mutations[mutationTypes.COMUM.SET_RETRAIR_MENU]).toHaveBeenCalled()
        })

        it('Deve setar o id do patrimonio', () => {
            wrapper = shallowMount(PatrimonioEdicao, {
                localVue,
                router,
                store: new Vuex.Store({ state, mutations, actions }),
                mocks: {
                    errors
                }
            })

            expect(wrapper.vm.patrimonioId).toEqual(patrimonio.id)
        })

        it('Deve verificar qual o passo atual', () => {
            wrapper = shallowMount(PatrimonioEdicao, {
                localVue,
                router,
                store: new Vuex.Store({ state, mutations, actions }),
                mocks: {
                    errors
                }
            })

            expect(wrapper.vm.passoAtual.numero).toEqual(1)
        })
    })

    describe('Methods', () => {

        it('Deve ir para rota de edicao ao clicar no passo', () => {
            wrapper = shallowMount(PatrimonioEdicao, {
                localVue,
                router,
                store: new Vuex.Store({ state, mutations, actions }),
                mocks: {
                    errors
                }
            })

            wrapper.vm.tratarEventoClick(passo)

            expect(router.push.mock.calls[0][0]).toEqual({ name: 'TipoEdicao' })
        })

        it('Deve deletar o patrimonio com sucesso', async () => {
            wrapper = shallowMount(PatrimonioEdicao, {
                localVue,
                router,
                store: new Vuex.Store({ state, mutations, actions }),
                mocks: {
                    errors
                }
            })

            wrapper.vm.tratarEventoDeletarPatrimonio()

            await flushPromises()

            expect(actions[actionTypes.PATRIMONIO.DELETAR_PATRIMONIO]).toHaveBeenCalled()
            expect(mutations[mutationTypes.LOKI.SHOW_ALERT]).toHaveBeenCalled()
            expect(router.push.mock.calls[0][0]).toEqual({ name: wrapper.vm.rotaOrigem })
        })

        it('Deve deletar o patrimonio', async () => {
            wrapper = shallowMount(PatrimonioEdicao, {
                localVue,
                router,
                store: new Vuex.Store({ state, mutations, actions }),
                mocks: {
                    errors
                }
            })

            wrapper.vm.tratarEventoDeletarPatrimonio()

            await flushPromises()

            expect(actions[actionTypes.PATRIMONIO.DELETAR_PATRIMONIO].mock.calls[0][1]).toEqual(48)
        })
    })

    describe('Destroyed', () => {
        it('Deve expandir a barra lateral ao ser destruido', () => {
            wrapper = shallowMount(PatrimonioEdicao, {
                localVue,
                router,
                store: new Vuex.Store({ state, mutations, actions }),
                mocks: {
                    errors
                }
            })

            wrapper.vm.$destroy()

            expect(actions[actionTypes.PATRIMONIO.BUSCAR_PATRIMONIO_POR_ID]).toHaveBeenCalled()
            expect(actions[actionTypes.PATRIMONIO.DELETAR_PATRIMONIO_NAO_ALTERADO]).toHaveBeenCalled()
            expect(mutations[mutationTypes.COMUM.SET_RETRAIR_MENU]).toHaveBeenCalled()
        })
    })
})
