import {shallowMount} from '@vue/test-utils'
import MovimentacaoListagemTabela from './MovimentacaoListagemTabela'
import Vuex from 'vuex'
import {mutationTypes} from '@/core/constants'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'

describe('MovimentacaoListagemTabela.vue', () => {

    let localVue, wrapper, store, mutations

    beforeEach(() => {
        localVue = applicationTestBuilder.build()

        mutations = {
            [mutationTypes.MOVIMENTACAO.RESETA_PAGE]: jest.fn()
        }

        store = new Vuex.Store({mutations})
    })

    describe('Watch', () => {
        it('deve emitir o evento de paginar quando alterar a paginação', () => {
            wrapper = shallowMount(MovimentacaoListagemTabela, {
                localVue,
                propsData: {
                    paginacao: {
                        page: 1,
                        rowsPerPage: 10,
                        descending: false,
                    },
                    paginas: 0,
                    totalItens: 0
                }
            })

            wrapper.vm.paginacaoInterna.rowsPerPage = 20
            expect(wrapper.emitted().paginar)
        })
    })

    describe('methods', () => {

        it('deve resetar a paginação', () => {
            wrapper = shallowMount(MovimentacaoListagemTabela, {
                localVue,
                store,
                propsData: {
                    itens: [
                        {
                            id: 1,
                            codigo: '00001'
                        }
                    ],
                    paginacao: {
                        page: 1,
                        rowsPerPage: 10,
                        descending: false,
                    },
                    paginas: 1,
                    totalItens: 1,
                }
            })
            wrapper.vm.resetaPage()

            expect(mutations[mutationTypes.MOVIMENTACAO.RESETA_PAGE]).toHaveBeenCalled()
        })

        it('deve emitir o evento de acessar passando a movimentação clicada por parâmetro', () => {
            const item = {
                id: 1
            }
            wrapper = shallowMount(MovimentacaoListagemTabela, {
                localVue,
                store,
                propsData: {
                    itens: [
                        {
                            id: 1,
                            codigo: '00001'
                        }
                    ],
                    paginacao: {
                        page: 1,
                        rowsPerPage: 10,
                        descending: false,
                    },
                    paginas: 1,
                    totalItens: 1,
                }
            })
            wrapper.vm.tratarClick(item)

            expect(wrapper.emitted().acessar).toBeTruthy()
            expect(wrapper.emitted().acessar[0]).toEqual([item])
        })

        it('deve formatar o órgão destino e retornar a sigla', () => {
            const item = {
                id: 1,
                orgaoDestino: 'AEM-MS'
            }
            wrapper = shallowMount(MovimentacaoListagemTabela, {
                localVue,
                store,
                propsData: {
                    itens: [
                        {
                            id: 1,
                            codigo: '00001'
                        }
                    ],
                    paginacao: {
                        page: 1,
                        rowsPerPage: 10,
                        descending: false,
                    },
                    paginas: 1,
                    totalItens: 1,
                }
            })

            expect(wrapper.vm.formatarOrgaoDestino(item)).toEqual('AEM-MS')
        })

        it('deve formatar o órgão destino e retornar um - quando o órgão destino estiver vazio', () => {
            const item = {
                id: 1,
                orgaoDestino: null
            }
            wrapper = shallowMount(MovimentacaoListagemTabela, {
                localVue,
                store,
                propsData: {
                    itens: [
                        {
                            id: 1,
                            codigo: '00001'
                        }
                    ],
                    paginacao: {
                        page: 1,
                        rowsPerPage: 10,
                        descending: false,
                    },
                    paginas: 1,
                    totalItens: 1,
                }
            })

            expect(wrapper.vm.formatarOrgaoDestino(item)).toEqual('-')
        })

        it('deve retornar true se a situação da movimentação for igual a EM_ELABORACAO', () => {
            const item = {
                situacao: 'EM_ELABORACAO'
            }
            wrapper = shallowMount(MovimentacaoListagemTabela, {
                localVue,
                store,
                propsData: {
                    itens: [
                        {
                            id: 1,
                            codigo: '00001'
                        }
                    ],
                    paginacao: {
                        page: 1,
                        rowsPerPage: 10,
                        descending: false,
                    },
                    paginas: 1,
                    totalItens: 1,
                }
            })

            expect(wrapper.vm.ehSituacaoEmElaboracao(item)).toEqual(true)
        })

        it('não deve retornar true se a situação da movimentação for diferente de EM_ELABORACAO', () => {
            const item = {
                situacao: 'FINALIZADO'
            }
            wrapper = shallowMount(MovimentacaoListagemTabela, {
                localVue,
                store,
                propsData: {
                    itens: [
                        {
                            id: 1,
                            codigo: '00001'
                        }
                    ],
                    paginacao: {
                        page: 1,
                        rowsPerPage: 10,
                        descending: false,
                    },
                    paginas: 1,
                    totalItens: 1,
                }
            })

            expect(wrapper.vm.ehSituacaoEmElaboracao(item)).toEqual(false)
        })
    })
})
