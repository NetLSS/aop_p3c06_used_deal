package com.lilcode.aop.p3.c06.used_deal.myPage

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.lilcode.aop.p3.c06.used_deal.R
import com.lilcode.aop.p3.c06.used_deal.databinding.FragmentMypageBinding

class MyPageFragment : Fragment(R.layout.fragment_mypage) {


    private lateinit var binding: FragmentMypageBinding

    private val auth: FirebaseAuth by lazy {
        Firebase.auth
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentMypageBinding = FragmentMypageBinding.bind(view)
        binding = fragmentMypageBinding

        initSignInOutButton()

        initSignUpButton()

        initEmailEditText()

        initPasswordEditText()
    }

    private fun initPasswordEditText() {
        // 이메일 및 패스워드가 비어있지 않을 때만 로그인 관련 버튼 활성화
        binding.passwordEditText.addTextChangedListener {
            val enable =
                binding.emailEditText.text.isNotEmpty() && binding.passwordEditText.text.isNotEmpty()
            binding.signUpButton.isEnabled = enable
            binding.signInOutButton.isEnabled = enable
        }
    }

    private fun initEmailEditText() {
        // 이메일 및 패스워드가 비어있지 않을 때만 로그인 관련 버튼 활성화
        binding.emailEditText.addTextChangedListener {
            val enable =
                binding.emailEditText.text.isNotEmpty() && binding.passwordEditText.text.isNotEmpty()
            binding.signUpButton.isEnabled = enable
            binding.signInOutButton.isEnabled = enable
        }
    }

    private fun initSignUpButton() {
        binding.signUpButton.setOnClickListener {

            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity()){ task->
                    if(task.isSuccessful){
                        Toast.makeText(context, "회원가입에 성공했습니다. 로그인 버튼을 눌러주세요.", Toast.LENGTH_SHORT)
                            .show()
                    }else{
                        Toast.makeText(context, "회원가입에 실패했습니다. 이미 가입된 이메일일 수 있습니다.", Toast.LENGTH_SHORT)
                            .show()
                    }

                }
        }
    }

    private fun initSignInOutButton() {

        binding.signInOutButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            if (auth.currentUser == null) {
                // 로그인
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(requireActivity()){ task ->
                        if(task.isSuccessful){
                            successSignIn()
                        }else{
                            Toast.makeText(context, "로그인에 실패했습니다. 이메일 또는 비밀번호를 확인해주세요.", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
            } else {
                //로그 아웃
                auth.signOut()
                binding.emailEditText.text.clear()
                binding.emailEditText.isEnabled = true
                binding.passwordEditText.text.clear()
                binding.passwordEditText.isEnabled = true

                binding.signInOutButton.text = "로그인"
                binding.signInOutButton.isEnabled = false
                binding.signUpButton.isEnabled = false
            }
        }
    }

    override fun onStart() {
        super.onStart()

        if(auth.currentUser == null){
            binding.emailEditText.text.clear()
            binding.emailEditText.isEnabled = true
            binding.passwordEditText.text.clear()
            binding.passwordEditText.isEnabled = true

            binding.signInOutButton.text = "로그인"
            binding.signInOutButton.isEnabled = false
            binding.signUpButton.isEnabled = false
        }else{
            binding.emailEditText.setText(auth.currentUser!!.email)
            binding.emailEditText.isEnabled = false
            binding.passwordEditText.setText("**********")
            binding.passwordEditText.isEnabled = false

            binding.signInOutButton.text = "로그아웃"
            binding.signInOutButton.isEnabled = true
            binding.signUpButton.isEnabled = false
        }
    }

    private fun successSignIn(){
        if(auth.currentUser == null){
            Toast.makeText(context, "로그인에 실패했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT)
                .show()
            return
        }

        binding.emailEditText.isEnabled = false
        binding.passwordEditText.isEnabled = false
        binding.signUpButton.isEnabled = false
        binding.signInOutButton.text = "로그아웃"
    }
}