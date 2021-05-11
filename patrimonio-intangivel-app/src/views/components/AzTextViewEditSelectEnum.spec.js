import {mount} from '@vue/test-utils'
import AzTextViewEditSelectEnum from "@/views/components/AzTextViewEditSelectEnum";
import applicationTestBuilder from '@/application/ApplicationTestBuilder'
import flushPromises from "flush-promises";

describe('AzTextViewEditSelectEnum', () => {
    let wrapper,localVue,$validator,errors

    beforeEach(()=>{
        localVue = applicationTestBuilder.build()
        errors = {
            collect:jest.fn()
        }
        $validator = {
            _base:{
                validateAll: jest.fn().mockReturnValue(true),
                errors:{
                    clear: jest.fn()
                }
            }
        }
    })

    describe('Emitted', ()=>{
        it('Deve setar a variavel editting para true e emitir um evento de edição', async()=>{
            wrapper = mount(AzTextViewEditSelectEnum,{
                localVue,
                mocks:{
                    $validator,
                    errors
                }
            })
            wrapper.vm.startEdit()
            await flushPromises()
            expect(wrapper.vm.editing).toEqual(true)
            expect(wrapper.emitted().setaEditando).toBeTruthy()
        })
        it('Deve emitir um evento para submeter edicao', async()=>{
            wrapper = mount(AzTextViewEditSelectEnum,{
                localVue,
                mocks:{
                    $validator,
                    errors
                }
            })
            wrapper.vm.submitEdit()
            await flushPromises()
            expect(wrapper.vm.editing).toEqual(false)
            expect(wrapper.emitted().retiraEditando).toBeTruthy()
            expect(wrapper.emitted().input).toBeTruthy()
        })
        it('Deve emitir um evento para cancelar  edicao', async()=>{
            wrapper = mount(AzTextViewEditSelectEnum,{
                localVue,
                mocks:{
                    $validator,
                    errors
                }
            })
            wrapper.vm.cancelEdit()
           await flushPromises()
            expect(wrapper.vm.editing).toEqual(false)
            expect(wrapper.emitted().retiraEditando).toBeTruthy()
        })
    })
})
