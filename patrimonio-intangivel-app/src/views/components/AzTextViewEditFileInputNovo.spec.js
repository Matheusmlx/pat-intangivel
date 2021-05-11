import {shallowMount} from '@vue/test-utils'
import AzTextViewEditFileInputNovo from '@/views/components/AzTextViewEditFileInputNovo'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import Vuex from 'vuex'
import flushPromises from 'flush-promises'
import {mutationTypes} from '@/core/constants'

describe('AzTextViewEditFileInputNovo', () => {
    let wrapper, localVue, $validator, errors, state, vuetify, store, mutations

    beforeEach(() => {
        localVue = applicationTestBuilder.build()
        vuetify = applicationTestBuilder.getVuetify()

        errors = {
            collect: jest.fn()
        }

        state = {
            model: undefined,
            editing: true,
            errorTamanhoExcedido: 0
        }

        $validator = {
            _base: {
                validateAll: jest.fn().mockReturnValue(true),
                errors: {
                    clear: jest.fn(),
                    collect: jest.fn()
                },
            }
        }

        mutations = {
            [mutationTypes.LOKI.SHOW_ALERT]: jest.fn(),
        }


        store = new Vuex.Store({state, mutations})
    })

    const defaultShallMount = () => shallowMount(AzTextViewEditFileInputNovo, {
        localVue,
        vuetify,
        store: new Vuex.Store({state, mutations}),
        propsData: {},
        mocks: {$validator, errors}
    })

    describe('Methods', () => {
        it('Deve chamar o metodo startEdit', () => {
            wrapper = defaultShallMount()

            wrapper.vm.startEdit()

            expect(wrapper.vm.model).toEqual(null)
            expect(wrapper.vm.editing).toEqual(true)
        })

        it('Deve retornar true caso o tamanho do Anexo seja maior do limite', () => {
            wrapper = defaultShallMount()
            expect(wrapper.vm.validarTamanhoMaximo(15116247)).toBe(true)
        })

        it('Deve retornar false caso o tamanho do Anexo seja menor do limite', () => {
            wrapper = defaultShallMount()
            expect(wrapper.vm.validarTamanhoMaximo(15116242)).toBe(false)
        })

        it('Deve retornar 0 caso a extensao do arquivo seja valida', async () => {
            wrapper = defaultShallMount()
            expect(wrapper.vm.extensaoArquivoValida({name: 'DocumentoExemplo.pdf'})).toBe(0)
        })

        it('Deve retornar -1 caso a extensao do arquivo seja invalida', async () => {
            wrapper = defaultShallMount()
            expect(wrapper.vm.extensaoArquivoValida({name: 'DocumentoExemplo.mp4'})).toBe(-1)
        })

        it('Deve emitir um emite de input caso a extensao seja valida e o tamanho do arquivo seja valido', async () => {
            wrapper = defaultShallMount()
            const documento = {
                name: 'DocumentoTeste.pdf',
                size: 15116238
            }
            wrapper.vm.model = documento

            wrapper.vm.submitEdit()
            await flushPromises()

            expect(wrapper.vm.editing).toEqual(false)
            expect(wrapper.emitted().input).toBeTruthy()
            expect(wrapper.emitted().input.length).toBe(1)
            expect(wrapper.emitted().input[0]).toEqual([documento])
        })

        it('Deve exibir um alert caso a extensão do arquivo seja invalida', async () => {
            wrapper = defaultShallMount()
            const documento = {
                name: 'MusicaTeste.mp3',
                size: 15116238
            }
            wrapper.vm.model = documento

            wrapper.vm.submitEdit()
            await flushPromises()

            expect(wrapper.vm.model).toEqual(null)
            expect(mutations[mutationTypes.LOKI.SHOW_ALERT].mock.calls[0][1].message).toEqual('Extensão do arquivo inválida. Envie arquivos nos seguintes formatos .pdf .jpg .png')
            expect(mutations[mutationTypes.LOKI.SHOW_ALERT].mock.calls[0][1].type).toEqual('error')
        })

        it('Deve anular o model caso o tamanho do arquivo passe do limite', async () => {
            wrapper = defaultShallMount()
            const documento = {
                name: 'DocumentoTeste.pdf',
                size: 15116267
            }
            wrapper.vm.model = documento

            wrapper.vm.submitEdit()
            await flushPromises()

            expect(wrapper.vm.model).toEqual(null)
            expect(wrapper.vm.errorTamanhoExcedido).toEqual(1)
        })
    })
})
