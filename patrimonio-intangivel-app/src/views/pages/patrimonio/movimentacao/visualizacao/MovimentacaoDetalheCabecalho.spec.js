import {shallowMount} from '@vue/test-utils'
import MovimentacaoDetalheCabecalho from './MovimentacaoDetalheCabecalho'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import Vuex from 'vuex'

describe('MovimentacaoDetalheCabecalho.vue', () => {

    let localVue, wrapper, state, store

    const movimentacao = {
        codigo: '00001',
        dataCadastro: '2020-10-30T11:01:08.198-0400',
        dataDeEnvio: '2020-08-30T11:01:21.544-0400',
        dataDeFinalizacao: null,
        dataNL: '2020-10-24T00:00:00.000-0400',
        id: 1,
        motivo: 'motivo teste',
        motivoRejeicao: null,
        numeroNL: '2115NL155455',
        orgaoDestino: {
            descricao: 'AEM-MS - Agência Estadual de Metrologia',
            id: 129,
            nome: 'Agência Estadual de Metrologia',
            sigla: 'AEM-MS'
        },
        orgaoOrigem: {
            descricao: 'AEM-MS - Agência Estadual de Metrologia',
            id: 129,
            nome: 'Agência Estadual de Metrologia',
            sigla: 'AEM-MS'
        },
        patrimonio: {
            id: 129,
            nome: 'Agência Estadual de Metrologia',
            numero: '000000001',
            setor: {
                descricao: 'ASCOM - Assessoria de Comunicação Social',
                id: 1
            }
        },
        situacao: 'AGUARDANDO_RECEBIMENTO',
        tipo: 'TRANSFERENCIA_DEFINITIVA',
        usuarioCadastro: 'admin'
    }

    beforeEach(() => {
        localVue = applicationTestBuilder.build()

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
            }
        }

        store = new Vuex.Store({state})
    })

    describe('renderização', () => {

        it('deve renderizar a toolbar com as informações da movimentação', () => {
            wrapper = shallowMount(MovimentacaoDetalheCabecalho, {
                localVue,
                store,
                propsData: {
                    movimentacao: movimentacao
                }
            })

            expect(wrapper.findAll('v-col-stub').at(0).text()).toContain('Código')
            expect(wrapper.findAll('v-col-stub').at(1).text()).toContain('Criado por')
            expect(wrapper.findAll('v-col-stub').at(2).text()).toContain('Data de Envio')
            expect(wrapper.findAll('v-col-stub').at(3).text()).toContain('Data de Finalização')
            expect(wrapper.findAll('v-col-stub').at(4).text()).toContain('Situação')
            expect(wrapper.findAll('p').at(0).text()).toContain('00001')
            expect(wrapper.findAll('p').at(1).text()).toContain('admin')
            expect(wrapper.findAll('p').at(2).text()).toContain('30/08/2020')
            expect(wrapper.findAll('p').at(3).text()).toContain('-')
            expect(wrapper.findAll('p').at(4).text()).toContain('Aguardando recebimento')
        })
    })

    describe('computed', () => {

        it('deve retornar false quando a situação da movimentação for diferente de FINALIZADO', () => {
            wrapper = shallowMount(MovimentacaoDetalheCabecalho, {
                localVue,
                store,
                propsData: {
                    movimentacao: movimentacao
                }
            })

            expect(wrapper.vm.ehMovimentacaoFinalizada).toEqual(false)
        })

        it('deve retornar true quando a situação da movimentação for igual a FINALIZADO', () => {
            wrapper = shallowMount(MovimentacaoDetalheCabecalho, {
                localVue,
                store,
                propsData: {
                    movimentacao: {
                        situacao: 'FINALIZADO'
                    }
                }
            })

            expect(wrapper.vm.ehMovimentacaoFinalizada).toEqual(true)
        })

        it('deve retornar false quando a situação da movimentação for diferente de REJEITADO', () => {
            wrapper = shallowMount(MovimentacaoDetalheCabecalho, {
                localVue,
                store,
                propsData: {
                    movimentacao: movimentacao
                }
            })

            expect(wrapper.vm.ehMovimentacaoRejeitada).toEqual(false)
        })

        it('deve retornar true quando a situação da movimentação for igual a REJEITADO', () => {
            wrapper = shallowMount(MovimentacaoDetalheCabecalho, {
                localVue,
                store,
                propsData: {
                    movimentacao: {
                        situacao: 'REJEITADO'
                    }
                }
            })

            expect(wrapper.vm.ehMovimentacaoRejeitada).toEqual(true)
        })
    })
})
