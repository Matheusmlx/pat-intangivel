import {shallowMount} from '@vue/test-utils'
import AzTextViewEditFileInput from '@/views/components/AzTextViewEditFileInput'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import flushPromises from 'flush-promises'
import Vuex from 'vuex'

describe('AzTextViewEditFileInput', () => {
    let wrapper, localVue, $validator, errors, state, vuetify, store

    beforeEach(() => {
        localVue = applicationTestBuilder.build()
        vuetify = applicationTestBuilder.getVuetify()

        errors = {
            collect: jest.fn()
        }

        state = {
            estaAdicionando: false,
            editing: false,
            model: undefined,
            errorObrigatoriedadeAnexo: 0,
            errorTamanhoExcedido: 0,

            loki: {
                user: {
                    authorities: [{name: 'Patrimonio.Normal', hasAccess: true}]
                },
                file: {
                    api: 'api'
                }
            }
        }

        $validator = {
            _base: {
                validateAll: jest.fn().mockReturnValue(true),
                errors: {
                    clear: jest.fn(),
                },
            }
        }

        store = new Vuex.Store({state})
    })

    const defaultShallMount = () => shallowMount(AzTextViewEditFileInput, {
        localVue,
        vuetify,
        store: new Vuex.Store({state}),
        propsData: {estaAdicionando: false},
        mocks: {$validator, errors}
    })

    describe('Methods', () => {
        it('Deve emitir um evento de estaAdicionando caso a propriedade estaAdicionando for true', async () => {
            wrapper = shallowMount(AzTextViewEditFileInput, {
                localVue,
                store,
                mocks: {
                    $validator,
                    errors
                },
                propsData: {
                    estaAdicionando: true
                }
            })

            wrapper.vm.startEdit()
            await flushPromises()

            expect(wrapper.vm.estaAdicionando).toEqual(true)
            expect(wrapper.emitted().estaAdicionando).toBeTruthy()
        })

        it('Deve emitir um evento de setaEditando caso a propriedade estaAdicionando for false', async () => {
            wrapper = defaultShallMount()

            wrapper.vm.startEdit()
            await flushPromises()

            expect(wrapper.vm.editing).toEqual(true)
            expect(wrapper.emitted('setaEditando', 'nameTest')).toBeTruthy()
        })

    })
})
