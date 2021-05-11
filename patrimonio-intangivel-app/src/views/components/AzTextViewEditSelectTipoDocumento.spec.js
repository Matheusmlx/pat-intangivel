import {mount} from '@vue/test-utils'
import AzTextViewEditSelectTipoDocumento from './AzTextViewEditSelectTipoDocumento'
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import flushPromises from 'flush-promises'

describe('AzTextViewEditSelectTipoDocumento', () => {
    let wrapper, localVue, $validator, errors

    beforeEach(() => {
        localVue = applicationTestBuilder.build()
        errors = {
            collect: jest.fn()
        }
        $validator = {
            _base: {
                validateAll: jest.fn().mockReturnValue(true),
                errors: {
                    clear: jest.fn(),
                },
            }
        }

    })

    describe('Methods', () => {
        it('Deve emitir um evento de estar adicionando um valor', async () => {
            wrapper = mount(AzTextViewEditSelectTipoDocumento, {
                localVue,
                mocks: {
                    $validator,
                    errors
                }
            })
            wrapper.vm.startEdit()
            await flushPromises()
            expect(wrapper.vm.editing).toEqual(true)
        })
        it('Deve emitir um evento quando estiver retirando a edição', async () => {
            wrapper = mount(AzTextViewEditSelectTipoDocumento, {
                localVue,
                mocks: {
                    $validator,
                    errors
                }
            })
            wrapper.vm.cancelEdit()
            await flushPromises()
            expect(wrapper.vm.editing).toEqual(false)
        })
        it('Deve emitir um evento para submeter edição', async () => {
            wrapper = mount(AzTextViewEditSelectTipoDocumento, {
                localVue,
                mocks: {
                    $validator,
                    errors
                }
            })
            wrapper.vm.submitEdit()
            await flushPromises()
            expect(wrapper.emitted().retiraEditando).toBeTruthy()
            expect(wrapper.vm.editing).toEqual(false)
        })
    })
})
