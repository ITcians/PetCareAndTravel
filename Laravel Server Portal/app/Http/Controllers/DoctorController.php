<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

use App\Models\Doctor;
use Exception;
use Illuminate\Support\Facades\Hash;
use stdClass;


class DoctorController extends Controller
{
    function index()
    {
        return view('doctors.index', ['doctors' => Doctor::all()->sortByDesc("doctorId")]);
    }
    function addDoctor(Request $request)
    {
        return view('doctors.doctorform');
    }
    function addDoctorProcess(Request $request)
    {
        try { 
            $data = $request->input();
            $doc = new Doctor();
            $doc->doctorName = $data['doctorname'];
            $doc->doctorAddress = $data['doctoraddress'];
            $doc->specification = $data['specification'];
            $doc->contact = $data['contact'];
            $doc->save();
            return redirect()->action([DoctorController::class, 'index'])->with('msg' , "Record Add Successfully!");
        } catch (Exception $ex) {
            return redirect()->action([DoctorController::class, 'index'])->with('msg' , $ex->getMessage());
        }
    }
    public function deleteDoctor($doctorId)
    {
        $delete_data=Doctor::where("doctorId", "=", $doctorId)->delete();
        if(isset($delete_data)){
           return redirect()->action([DoctorController::class, 'index'])->with('msg' , "Record Delete Successfully!");
        }else{
            return redirect()->action([DoctorController::class, 'index'])->with('msg' , "Try Again");
        }
    }
    public function editDoctor($doctorId)
    {
        $data["doctor"] = Doctor::where("doctorId","=",$doctorId)->get()->first();
        return view("doctors.doctorformupdate",$data);
    }
    public function updateDoctorProcess(Request $request)
    {
        $data=[
            "doctorName"=>$request->input("doctorname"),
            "doctorAddress"=>$request->input("doctoraddress"),
            "specification"=>$request->input("specification"),
            "contact"=>$request->input("contact"),
        ];
         $edit_data= Doctor::where("doctorId", "=", $request->input("doctorId"))->update($data);
        if(isset($edit_data))
        {
            return redirect()->action([DoctorController::class, 'index'])->with('msg' , "Record Update Successfully!");
        }else{
                return redirect()->action([DoctorController::class, 'index'])->with('msg' , "Try Again");
            }
    }
  

    function search(Request $req){
        return Doctor::where('doctorName','like','%'.$req->input('term').'%')
        ->orWhere('specification','like','%'.$req->input('term').'%')
        ->get();
    }

}
