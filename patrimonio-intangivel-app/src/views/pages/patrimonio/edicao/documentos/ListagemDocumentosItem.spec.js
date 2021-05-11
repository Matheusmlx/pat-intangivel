import {shallowMount} from '@vue/test-utils'
import ListagemDocumentosItem from './ListagemDocumentosItem'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import Vuex from 'vuex'
import {mask} from 'vue-the-mask'
import flushPromises from 'flush-promises'
import camposPersonalizadosDefault from "../../../../../core/store/camposPersonalizados/camposPersonalizadosDefault";

describe('ListagemDocumentosItem', () => {
    let wrapper, localVue, errors, $validator, store,mutations,actions, state, directives, $t = jest.fn(), windowSpy

    localVue = applicationTestBuilder.build()

    directives = {
        mask: {
            ...mask,
            tokens: {
                ...mask.tokens,
                '*': /./,
            }
        },
    }

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
    }

    const defaultMount = (stubs) => shallowMount(ListagemDocumentosItem, {
        localVue,
        directives,
        stubs,
        store: new Vuex.Store({
            state, mutations, actions,
            modules: {
                CamposPersonalizados
            }
        }),
        mocks: {$validator, errors, $t},
    })

    const CamposPersonalizados = {
        namespaced: true,
        actions: {getAllCamposPersonlizados: jest.fn()},
        getters: {
            getNomeCamposPersonalizados: () => jest.fn(),
            getObrigatorioCamposPersonalizados: () => jest.fn(),
        }
    }

    const vueTextFieldStub = {
        render: () => {
        },
        methods: {
            focus: () => {
            }
        }
    }

    beforeEach(async () => {
        state = {
            regrasAnexo: '',
            nomeAnexo: '',
            deletarBotao: 'grey',
            apagar: true,
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
                situacaoVindora: '',
            },
            value: [{numero: null}]
        }
        store = new Vuex.Store(({state}))

        windowSpy = jest.spyOn(global, 'window', 'get')
        windowSpy.mockImplementation(() => ({
            location: jest.fn(),
            getComputedStyle: jest.fn(),
            addEventListener: jest.fn(),
            removeEventListener: jest.fn(),
            $i18n: {
                locale: 'camposPersonalizados',
                getLocaleMessage: () => camposPersonalizadosDefault.i18n
            }
        }))
    })

    describe('Methods', () => {
        it('Deve enviar ao componente pai um emit de salvar se documento conter dados obrigatorios para tipo Nota Fiscal', async () => {
            wrapper = defaultMount()
            wrapper.vm.tipoDocumento = [{id: 1, descricao: 'Nota Fiscal'}]
            wrapper.vm.value = {numero: '1', valor: 600, data: '05/11/2016', uriAnexo: 'anexo', idTipoDocumento: 1}
            wrapper.vm.tratarEventoEdicaoCampo()
            wrapper.vm.estaValidado = true
            await flushPromises()

            expect(wrapper.vm.estaValidado).toEqual(true)
            expect(wrapper.emitted().salvar).toBeTruthy()
        })

        it('Não Deve enviar ao componente pai um emit de salvar se documento não conter valor para tipo Nota Fiscal', async () => {
            wrapper = defaultMount()
            wrapper.vm.tipoDocumento = [{id: 1, descricao: 'Nota Fiscal'}]
            wrapper.vm.value = {numero: '1', valor: null, data: '05/11/2016', uriAnexo: 'anexo', idTipoDocumento: 1}
            wrapper.vm.tratarEventoEdicaoCampo()
            wrapper.vm.estaValidado = true
            await flushPromises()

            expect(wrapper.vm.estaValidado).toEqual(true)
            expect(wrapper.emitted().salvar).not.toBeTruthy()
        })
        it('Não Deve enviar ao componente pai um emit de salvar se documento não conter data para tipo Nota Fiscal', async () => {
            wrapper = defaultMount()
            wrapper.vm.tipoDocumento = [{id: 1, descricao: 'Nota Fiscal'}]
            wrapper.vm.value = {numero: '1', valor: null, data: null, uriAnexo: 'anexo', idTipoDocumento: 1}
            wrapper.vm.tratarEventoEdicaoCampo()
            wrapper.vm.estaValidado = false
            await flushPromises()

            expect(wrapper.vm.estaValidado).toEqual(false)
            expect(wrapper.emitted().salvar).not.toBeTruthy()
        })
        it('Deve enviar ao componente pai um emit de salvar se documento conter todos dados obrigatorios para tipo diferente de nota fiscal', async () => {
            wrapper = defaultMount()
            wrapper.vm.tipoDocumento = [{id: 1, descricao: 'Nota Fiscal'}]
            wrapper.vm.value = {numero: '1', valor: null, data: null, uriAnexo: 'anexo', idTipoDocumento: 2}
            wrapper.vm.tratarEventoEdicaoCampo()
            wrapper.vm.estaValidado = true
            await flushPromises()

            expect(wrapper.vm.estaValidado).toEqual(true)
            expect(wrapper.emitted().salvar).toBeTruthy()
        })
        it('Não deve emitir o evento salvar caso o estado estaValidado for igual a false', async () => {
            wrapper = defaultMount({'v-text-field': vueTextFieldStub})
            wrapper.vm.estaValidado = false
            wrapper.vm.tratarEventoEdicaoCampo()

            expect(wrapper.vm.estaValidado).toEqual(false)
            expect(wrapper.emitted().salvar).not.toBeTruthy()
        })
        it('Deve emitir um evento, baixarAnexo, ao clicar no botão de download', async () => {
            wrapper = wrapper = defaultMount({'v-text-field': vueTextFieldStub})
            wrapper.vm.baixarAnexo()
            wrapper.vm.$emit('baixarAnexo')
            await wrapper.vm.$nextTick()

            expect(wrapper.emitted().baixarAnexo).toBeTruthy()

        })
        it('Deve emitir um evento, removerDocumento, ao clicar no botão de excluir', async () => {
            wrapper = wrapper = defaultMount({'v-text-field': vueTextFieldStub})
            wrapper.vm.removerDocumento()
            wrapper.vm.$emit('removerDocumento')
            await wrapper.vm.$nextTick()

            expect(wrapper.emitted().removerDocumento).toBeTruthy()
        })
    })


})

