import {shallowMount} from '@vue/test-utils'
import ModalMovimentacao from './ModalMovimentacao'
import Vuex from 'vuex'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import flushPromises from 'flush-promises'

describe('ModalMovimentacao.vue', () => {

    let localVue, wrapper, state, store, $validator, errors

    const movimentacao = {
        id: 1,
        idPatrimonio: 6,
        motivo: 'Sem uso',
        orgaoDestino: 4,
        situacao: 'EM_ELABORACAO'
    }

    const tipo = 'TRANSFERENCIA_DEFINITIVA'

    beforeEach(() => {
        localVue = applicationTestBuilder.build()

        errors = {
            collect: jest.fn(),
        }

        $validator = {
            _base: {
                validateAll: jest.fn().mockReturnValue(true),
                errors: {
                    clear: jest.fn(),
                },

            },
            validate: jest.fn().mockReturnValue(true)
        }

        state = {
            camposAceitos: false,
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
            movimentacao: {
                id: 1,
                tipo: 'TRANSFERENCIA_DEFINITIVA',
                patrimonioId: 1
            },

            patrimonio: {
                patrimonio: {
                    id: 48,
                    nome: 'teste',
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
                    orgao: 3,
                    setor: 4,
                }
            }

        }

        store = new Vuex.Store({state})
    })

    describe('computed', () => {

        it('deve formatar o tipo da movimentação e retornar transferido definitivamente se for uma TRANSFERENCIA_DEFINITIVA', () => {
            wrapper = shallowMount(ModalMovimentacao, {
                localVue,
                store,
                propsData: {
                    movimentacao: movimentacao,
                    tipo: tipo
                },
                mocks: {$validator, errors}
            })

            expect(wrapper.vm.formatarTipoMovimentacao).toEqual('transferido definitivamente')
        })

        it('deve formatar o tipo da movimentação e retornar doado se for uma DOACAO_ENTRE_ORGAOS', () => {
            wrapper = shallowMount(ModalMovimentacao, {
                localVue,
                store,
                propsData: {
                    movimentacao: movimentacao,
                    tipo: 'DOACAO_ENTRE_ORGAOS'
                },
                mocks: {$validator, errors}
            })

            expect(wrapper.vm.formatarTipoMovimentacao).toEqual('doado')
        })
    })

    describe('methods', () => {

        it('deve emitir o evento de atualizar a movimentação para o componente pai', () => {
            wrapper = shallowMount(ModalMovimentacao, {
                localVue,
                store,
                propsData: {
                    movimentacao: movimentacao,
                    tipo: tipo
                },
                mocks: {$validator, errors}
            })

            wrapper.vm.atualizarMovimentacao()

            expect(wrapper.emitted().atualizarMovimentacao).toBeTruthy()
            expect(wrapper.emitted().atualizarMovimentacao[0]).toEqual([wrapper.vm.$props.movimentacao])
        })

        it('deve emitir o evento de tratarEventoDeletarMovimentacao para o componente pai', async () => {
            let movimentacaoImcompleta = {
                id: 1,
                idPatrimonio: 6,
                motivo: '',
                orgaoDestino: null,
                situacao: 'EM_ELABORACAO'

            }
            wrapper = shallowMount(ModalMovimentacao, {
                localVue,
                store,
                propsData: {
                    movimentacao: movimentacaoImcompleta,
                    tipo: tipo
                },
                mocks: {$validator, errors}
            })

            wrapper.vm.clicarForaModal()
            await flushPromises()

            expect(wrapper.emitted().tratarEventoDeletarMovimentacao).toBeTruthy()
            expect(wrapper.vm.estaDeletando).toEqual(true)
        })

        it('deve emitir o evento de atualizarMovimentacao para o componente pai',async () => {
            let movimentacaoImcompleta = {
                id: 1,
                idPatrimonio: 6,
                motivo: 'Sem Uso',
                orgaoDestino: null,
                situacao: 'EM_ELABORACAO'

            }
            wrapper = shallowMount(ModalMovimentacao, {
                localVue,
                store,
                propsData: {
                    movimentacao: movimentacaoImcompleta,
                    tipo: tipo
                },
                mocks: {$validator, errors}
            })

            wrapper.vm.clicarForaModal()
            await flushPromises()

            expect(wrapper.emitted().atualizarMovimentacao).toBeTruthy()
        })


        it('deve emitir o evento de fechar o modal de movimentação para o componente pai', () => {
            wrapper = shallowMount(ModalMovimentacao, {
                localVue,
                store,
                propsData: {
                    movimentacao: movimentacao,
                    tipo: tipo
                },
                mocks: {$validator, errors}
            })
            wrapper.vm.fecharModalMovimentacao()

            expect(wrapper.emitted().fecharModalMovimentacao).toBeTruthy()
        })

        it('Deve emitir um evento de enviarMovimentacao quando o metodo enviarMovimentacao for chamada', async () => {
            wrapper = shallowMount(ModalMovimentacao, {
                localVue,
                store,
                propsData: {
                    movimentacao: movimentacao,
                    tipo: tipo
                },
                mocks: {$validator, errors}
            })

            await wrapper.vm.enviarMovimentacao()

            expect(wrapper.emitted().enviarMovimentacao).toBeTruthy()
        })

        it('deve retornar o valor formatado na moeda brasileira', () => {
            const valorLiquido = 100.000000
            wrapper = shallowMount(ModalMovimentacao, {
                localVue,
                store,
                propsData: {
                    movimentacao: movimentacao,
                    tipo: tipo
                },
                mocks: {$validator, errors}
            })

            expect(wrapper.vm.returnToLocaleStringMoeda(valorLiquido)).toEqual('R$ 100,00')
        })

        it('não deve retornar o valor formatado na moeda brasileira se o valor recebido for nulo ou indefinido', () => {
            const valorLiquido = null
            wrapper = shallowMount(ModalMovimentacao, {
                localVue,
                store,
                propsData: {
                    movimentacao: movimentacao,
                    tipo: tipo
                },
                mocks: {$validator, errors}
            })

            expect(wrapper.vm.returnToLocaleStringMoeda(valorLiquido)).toEqual('----')
        })

        it('Deve emitir o evento de tratarEventoDeletarMovimentacao para o componente pai', async () => {
            wrapper = shallowMount(ModalMovimentacao, {
                localVue,
                store,
                propsData: {
                    movimentacao: movimentacao,
                    tipo: tipo,
                    situacao: movimentacao.situacao
                },
                mocks: {$validator, errors}
            })

            await wrapper.vm.tratarEventoDeletarMovimentacao()

            expect(wrapper.emitted().tratarEventoDeletarMovimentacao).toBeTruthy()
        })
    })
})
