import {shallowMount} from '@vue/test-utils'
import MovimentacaoDetalhe from './MovimentacaoDetalhe'
import Vuex from 'vuex'
import {actionTypes, mutationTypes} from '@/core/constants'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import flushPromises from 'flush-promises'
import moment from 'moment'

describe('MovimentacaoDetalhe.vue', () => {

    let localVue, wrapper, router, store, actions, mutations, $gtag

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
        patrimonio: 1,
        situacao: 'AGUARDANDO_RECEBIMENTO',
        tipo: 'TRANSFERENCIA_DEFINITIVA',
        usuarioCadastro: 'admin'
    }
    const patrimonio = {
        id: 129,
        nome: 'Agência Estadual de Metrologia',
        numero: '000000001',
        setor: 'ASCOM - Assessoria de Comunicação Social'
    }

    const items = {
        items: [{id: 129}, {id: 130}]
    }

    beforeEach(() => {
        localVue = applicationTestBuilder.build()

        router = {
            init: jest.fn(),
            push: jest.fn(),
            history: {
                current: {
                    name: 'MovimentacaoDetalhe',
                    params: {
                        id: 1
                    },
                    query:{rotaName:''}
                }
            }
        }

        $gtag = {
            event: jest.fn()
        }

        actions = {
            [actionTypes.MOVIMENTACAO.ATUALIZAR_MOVIMENTACAO]: jest.fn(),
            [actionTypes.MOVIMENTACAO.BUSCAR_MOVIMENTACAO_POR_ID]: jest.fn().mockReturnValue(movimentacao),
            [actionTypes.MOVIMENTACAO.BUSCAR_PATRIMONIO_POR_MOVIMENTACAO]: jest.fn().mockReturnValue(patrimonio),
            [actionTypes.MOVIMENTACAO.RECEBER_MOVIMENTACAO]: jest.fn().mockReturnValue(),
            [actionTypes.MOVIMENTACAO.REJEITAR_MOVIMENTACAO]: jest.fn().mockReturnValue(),
            [actionTypes.COMUM.BUSCAR_TODOS_ORGAOS]: jest.fn().mockReturnValue(items)
        }

        mutations = {
            [mutationTypes.LOKI.SHOW_ALERT]: jest.fn()
        }

        store = new Vuex.Store({actions, mutations})
    })

    describe('methods', () => {

        it('deve setar o id da movimentação de acordo com o valor recebido por parâmetro da rota', () => {
            wrapper = shallowMount(MovimentacaoDetalhe, {
                localVue,
                router,
                store,
                $gtag
            })

            wrapper.vm.setarMovimentacaoId()

            expect(wrapper.vm.movimentacaoId).toEqual(1)
        })

        it('deve buscar a movimentação por id', async () => {
            wrapper = shallowMount(MovimentacaoDetalhe, {
                localVue,
                router,
                store,
                $gtag
            })
            await flushPromises()
            await wrapper.vm.buscarMovimentacaoPorId()

            await flushPromises()

            expect(actions[actionTypes.MOVIMENTACAO.BUSCAR_MOVIMENTACAO_POR_ID]).toHaveBeenCalled()
            expect(actions[actionTypes.MOVIMENTACAO.BUSCAR_MOVIMENTACAO_POR_ID].mock.calls[0][1]).toEqual(wrapper.vm.$data.movimentacaoId)
            expect(wrapper.vm.movimentacao).toEqual(movimentacao)
        })

        it('deve buscar o patrimônio pelo id do patrimônio', async () => {
            wrapper = shallowMount(MovimentacaoDetalhe, {
                localVue,
                router,
                store,
                $gtag
            })
            wrapper.setData({
                movimentacao: {
                    patrimonio: 1
                }
            })
            await wrapper.vm.buscarPatrimonioPorMovimentacao()

            await flushPromises()

            expect(actions[actionTypes.MOVIMENTACAO.BUSCAR_PATRIMONIO_POR_MOVIMENTACAO]).toHaveBeenCalled()
            expect(actions[actionTypes.MOVIMENTACAO.BUSCAR_PATRIMONIO_POR_MOVIMENTACAO].mock.calls[0][1]).toEqual(wrapper.vm.$data.movimentacao.patrimonio)
            expect(wrapper.vm.patrimonio).toEqual(patrimonio)
        })

        it('deve atualizar a movimentação', async () => {
            const movimentacaoAtualizar = {
                id: 1,
                idPatrimonio: 1,
                motivo: 'motivo teste',
                orgaoDestino: movimentacao.orgaoDestino,
                dataNL: '2020-10-24T00:00:00.000-0400',
                numeroNL: '2115NL155455'
            }
            wrapper = shallowMount(MovimentacaoDetalhe, {
                localVue,
                router,
                store,
                $gtag
            })

            await wrapper.vm.atualizar(movimentacaoAtualizar)

            await flushPromises()

            expect(actions[actionTypes.MOVIMENTACAO.ATUALIZAR_MOVIMENTACAO]).toHaveBeenCalled()
            expect(actions[actionTypes.MOVIMENTACAO.ATUALIZAR_MOVIMENTACAO].mock.calls[0][1]).toEqual(movimentacaoAtualizar)
        })

        it('deve validar se o mês atual é maior que o mês do envio da movimentação e mostrar notificação', () => {
            const mensagem = {message: 'Essa movimentação está sem recebimento desde o mês passado.', type: 'warning'}
            wrapper = shallowMount(MovimentacaoDetalhe, {
                localVue,
                router,
                store,
                $gtag
            })
            wrapper.setData({
                movimentacao: movimentacao
            })
            wrapper.vm.validarDataEnvioSemRecebimento()

            expect(mutations[mutationTypes.LOKI.SHOW_ALERT]).toHaveBeenCalled()
            expect(mutations[mutationTypes.LOKI.SHOW_ALERT].mock.calls[0][1]).toEqual(mensagem)
        })

        it('deve validar se o mês atual é maior que o mês do envio da movimentação e não mostrar notificação', () => {
            wrapper = shallowMount(MovimentacaoDetalhe, {
                localVue,
                router,
                store,
                $gtag
            })
            wrapper.setData({
                movimentacao: {
                    dataDeEnvio: moment()
                }
            })
            wrapper.vm.validarDataEnvioSemRecebimento()

            expect(mutations[mutationTypes.LOKI.SHOW_ALERT]).not.toHaveBeenCalled()
        })

        it('deve buscar todos os órgãos', async () => {
            wrapper = shallowMount(MovimentacaoDetalhe, {
                localVue,
                router,
                store,
                $gtag
            })

            await wrapper.vm.buscarOrgaos()

            await flushPromises()

            expect(actions[actionTypes.COMUM.BUSCAR_TODOS_ORGAOS]).toHaveBeenCalled()
        })

        it('deve validar se o órgão destino pertence há algum órgão e setar ehDestinatario como verdadeiro caso pertença', async () => {
            wrapper = shallowMount(MovimentacaoDetalhe, {
                localVue,
                router,
                store,
                $gtag
            })
            wrapper.setData({
                movimentacao: {
                    orgaoDestino: {
                        id: 130
                    }
                }
            })
            await flushPromises
            await wrapper.vm.validarEhDestinatario()

            await flushPromises
            expect(wrapper.vm.ehDestinatario).toEqual(true)
        })

        it('deve validar se o órgão destino pertence há algum órgão e setar ehDestinatario como falso caso não pertença', async () => {
            actions[actionTypes.COMUM.BUSCAR_TODOS_ORGAOS] = jest.fn().mockReturnValue({items: []})
            wrapper = shallowMount(MovimentacaoDetalhe, {
                localVue,
                router,
                $gtag,
                store: new Vuex.Store({mutations, actions})
            })
            await flushPromises
            await wrapper.vm.validarEhDestinatario()

            await flushPromises

            expect(wrapper.vm.ehDestinatario).toEqual(false)
        })

        it('deve receber a movimentacao', async () => {
            const id = 1
            const mensagem = {message: 'Movimentação recebida com sucesso', type: 'success'}
            wrapper = shallowMount(MovimentacaoDetalhe, {
                localVue,
                router,
                store,
                $gtag
            })

            await wrapper.vm.receberMovimentacao(id)

            await flushPromises()

            expect(actions[actionTypes.MOVIMENTACAO.RECEBER_MOVIMENTACAO]).toHaveBeenCalled()
            expect(actions[actionTypes.MOVIMENTACAO.RECEBER_MOVIMENTACAO].mock.calls[0][1]).toEqual(id)
            expect(mutations[mutationTypes.LOKI.SHOW_ALERT]).toHaveBeenCalled()
            expect(mutations[mutationTypes.LOKI.SHOW_ALERT].mock.calls[0][1]).toEqual(mensagem)
        })

        it('deve rejeitar a movimentacao', async () => {
            const id = 1
            const mensagem = {message: 'Movimentação rejeitada com sucesso', type: 'success'}
            wrapper = shallowMount(MovimentacaoDetalhe, {
                localVue,
                router,
                store,
                $gtag
            })

            await wrapper.vm.rejeitarMovimentacao(id)

            await flushPromises()

            expect(actions[actionTypes.MOVIMENTACAO.REJEITAR_MOVIMENTACAO]).toHaveBeenCalled()
            expect(actions[actionTypes.MOVIMENTACAO.REJEITAR_MOVIMENTACAO].mock.calls[0][1]).toEqual(id)
            expect(mutations[mutationTypes.LOKI.SHOW_ALERT]).toHaveBeenCalled()
            expect(mutations[mutationTypes.LOKI.SHOW_ALERT].mock.calls[0][1]).toEqual(mensagem)
        })
    })
})
