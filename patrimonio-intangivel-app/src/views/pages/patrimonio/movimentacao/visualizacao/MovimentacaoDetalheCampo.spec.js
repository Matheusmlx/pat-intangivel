import {shallowMount} from '@vue/test-utils'
import Vuex from 'vuex'
import MovimentacaoDetalheCampos from './MovimentacaoDetalheCampos'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'

describe('MovimentacaoDetalheCampos.vue', () => {

    let localVue, wrapper, store, vuetify, state

    const movimentacao = {
        codigo: '00001',
        dataCadastro: '2021-10-30T11:01:08.198-0400',
        dataDeEnvio: '2021-08-30T11:01:21.544-0400',
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
        patrimonio: 1,
        situacao: 'AGUARDANDO_RECEBIMENTO',
        tipo: 'TRANSFERENCIA_DEFINITIVA',
        usuarioCadastro: 'admin'
    }

    const CamposPersonalizados = {
        namespaced: true,
        actions: {getAllCamposPersonlizados: jest.fn()},
        getters: {
            getNomeCamposPersonalizados: () => jest.fn(),
            getObrigatorioCamposPersonalizados: () => jest.fn().mockReturnValue(true),
        }
    }

    beforeEach(() => {
        localVue = applicationTestBuilder.build()
        vuetify = applicationTestBuilder.getVuetify()

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

        store = new Vuex.Store({
            state,
            modules: {CamposPersonalizados}
        })
    })

    describe('computed', () => {

        it('deve retornar false quando a situação da movimentação for diferente de FINALIZADO', () => {
            wrapper = shallowMount(MovimentacaoDetalheCampos, {
                localVue,
                store,
                propsData: {
                    movimentacao: movimentacao,
                    patrimonio: {
                        id: 129,
                        nome: 'Agência Estadual de Metrologia',
                        numero: '000000001',
                        setor: 'ASCOM - Assessoria de Comunicação Social'
                    }
                }
            })
            expect(wrapper.vm.validarSeMovimentacaoFinalizada).toEqual(false)
        })

        it('deve retornar true quando a situação da movimentação for igual a FINALIZADO', () => {
            wrapper = shallowMount(MovimentacaoDetalheCampos, {
                localVue,
                store,
                propsData: {
                    movimentacao: {
                        orgaoDestino: {
                            descricao: 'AEM-MS - Agência Estadual de Metrologia'
                        },
                        orgaoOrigem: {
                            descricao: 'AEM-MS - Agência Estadual de Metrologia'
                        },
                        situacao: 'FINALIZADO'
                    },
                    patrimonio: {
                        id: 129,
                        nome: 'Agência Estadual de Metrologia',
                        numero: '000000001',
                        setor: 'ASCOM - Assessoria de Comunicação Social'
                    }
                }
            })

            expect(wrapper.vm.validarSeMovimentacaoFinalizada).toEqual(true)
        })

        it('deve retornar false quando a situação da movimentação for diferente de REJEITADO', () => {
            wrapper = shallowMount(MovimentacaoDetalheCampos, {
                localVue,
                store,
                propsData: {
                    movimentacao: movimentacao,
                    patrimonio: {
                        id: 129,
                        nome: 'Agência Estadual de Metrologia',
                        numero: '000000001',
                        setor: 'ASCOM - Assessoria de Comunicação Social'
                    }
                }
            })

            expect(wrapper.vm.validarSeMovimentacaoRejeitada).toEqual(false)
        })

        it('deve retornar true quando a situação da movimentação for igual a REJEITADO', () => {
            wrapper = shallowMount(MovimentacaoDetalheCampos, {
                localVue,
                store,
                propsData: {
                    movimentacao: {
                        orgaoDestino: {
                            descricao: 'AEM-MS - Agência Estadual de Metrologia'
                        },
                        orgaoOrigem: {
                            descricao: 'AEM-MS - Agência Estadual de Metrologia'
                        },
                        situacao: 'REJEITADO'
                    },
                    patrimonio: {
                        id: 129,
                        nome: 'Agência Estadual de Metrologia',
                        numero: '000000001',
                        setor: 'ASCOM - Assessoria de Comunicação Social'
                    }
                }
            })

            expect(wrapper.vm.validarSeMovimentacaoRejeitada).toEqual(true)
        })
    })

    describe('methods', () => {

        it('deve emitir o evento de atualizar para o componente pai', () => {
            wrapper = shallowMount(MovimentacaoDetalheCampos, {
                localVue,
                vuetify,
                store,
                propsData: {
                    movimentacao: movimentacao,
                    patrimonio: {
                        id: 129,
                        nome: 'Agência Estadual de Metrologia',
                        numero: '000000001',
                        setor: 'ASCOM - Assessoria de Comunicação Social'
                    }
                }
            })

            const campoNumeroNl = wrapper.find('az-text-view-edit')
            const campoTipo = wrapper.find('az-text-view-stub ')
            const azDate = wrapper.find('az-text-view-edit-date')

            expect(azDate.element.getAttribute('name')).toBe("Data NL")
            expect(azDate.element.getAttribute('required')).toBe("true")

            wrapper.vm.tratarEventoSalvar()
            expect(campoTipo.find('[label="Tipo"]').exists()).toBe(true)
            expect(campoNumeroNl.find('[name="Número NL"]').exists()).toBe(true)


            expect(wrapper.emitted().atualizar).toBeTruthy()
        })
    })
})
