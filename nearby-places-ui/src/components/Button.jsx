const Button = (props) => {
    const {disabled, onClick, label} = props;
  return (
    <button 
    type="button" 
    className="btn btn-success m-2" 
    disabled={disabled} 
    onClick={onClick}>{label}</button>
  )
}

export default Button